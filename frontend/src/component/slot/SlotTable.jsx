import React, { useState } from 'react';
import Modal from 'react-modal';
import { setSlotData } from '../../apis/setSlotData';
import excelLogo from './excel.png';
import { deleteSlot } from '../../apis/deleteSlot';
import * as XLSX from 'xlsx';

Modal.setAppElement('#root');

const SlotTable = ({ slots, accessToken, role }) => {
  
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const [currentSlot, setCurrentSlot] = useState(null);
  const [formData, setFormData] = useState({
    workKeyword: '',
    rankKeyword: '',
    mid: '',
    originMid: '',
    description: '',
    slotPaymentState: '' 
  });

  const openModal = (slot) => {
    setCurrentSlot(slot);
    setFormData({
      id: slot.id,
      workKeyword: slot.workKeyword,
      rankKeyword: slot.rankKeyword,
      mid: slot.mid,
      originMid: slot.originMid,
      description: slot.description,
      slotPaymentState: slot.slotPaymentState === 'Y' ? 'y' : 'n' 
    });
    setModalIsOpen(true);
  };

  const closeModal = () => {
    setModalIsOpen(false);
    setCurrentSlot(null);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSave = async () => {
    setSlotData(formData, accessToken).then(() =>{
      closeModal();
      alert('수정에 성공했습니다.');
    })
    .catch((error) => {
      alert(error.response.data.message);
    });
  };

  function isDateInRange(startAt, endAt) {
    const today = new Date();
    const startDate = new Date(startAt);
    startDate.setDate(startDate.getDay() - 1);
    const endDate = new Date(endAt);
  
    return startDate <= today && today <= endDate;
  }

  const handleDelete = (id) => {
    const confirmed = window.confirm('정말 삭제하시겠습니까?');
    if (confirmed) {
      deleteSlot(id, accessToken)
      .then(() => {
        closeModal();
        alert('삭제에 성공했습니다.');      
      })
      .catch((error) => {
        alert(error.response.data.message);
      });

    }
  };

  const getColorIndicator = (state, startAt, endAt) => {
    if (!isDateInRange(startAt, endAt)) {
      return 'black';
    } else if (state === 'Y') {
      return 'red';
    } else if (state === 'N') {
      return 'green';
    } else {
      return 'orange';
    }
  };

  const getPaymentStateText = (state) => {
    if (state === 'y') {
      return <span style={{ color: 'green', fontWeight: 'bold' }}>완료</span>;
    } else if (state === 'n') {
      return <span style={{ color: 'red', fontWeight: 'bold' }}>미결제</span>;
    } else {
      return null;
    }
  };

  const getStatusText = (color) => {
    if (color === 'green') {
      return '구동 중';
    } else if (color === 'red') {
      return '오류';
    } else if (color === 'black') {
      return '종료';
    } else if(color === 'orange') {
      return '미입력'
    } else {
      return '-';
    }
  }

  const exportToExcel = () => {
    const worksheet = XLSX.utils.json_to_sheet(slots.map(slot => ({
      번호: slot.id,
      상태: getStatusText(getColorIndicator(slot.slotErrorState, slot.startAt, slot.endAt)),
      작업키워드: slot.workKeyword,
      순위키워드: slot.rankKeyword,
      MID: slot.mid,
      원부MID: slot.originMid,
      시작일: slot.startAt,
      종료일: slot.endAt,
      메모: slot.description,
      담당계정: slot.adminId,
      오픈계정: slot.ownerId,
      ...(role === 'master' || role === 'admin' ? { 결제: slot.slotPaymentState === 'N' ? '미결제' : '결제 완료' } : {})
    })));
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, "Slots");
    XLSX.writeFile(workbook, "SlotsData.xlsx");
  };
  return (
    <div>
      <div className='excelWrap'>
        <img className='excelImg' src={excelLogo} alt="엑셀" onClick={exportToExcel} style={{ cursor: 'pointer' }} />
      </div>
      <table>
        <thead>
          <tr>
            <th>번호</th>
            <th>상태</th>
            <th>작업 키워드</th>
            <th>순위 키워드</th>
            <th>MID</th>
            <th>원부 MID</th>
            <th>시작일</th>
            <th>종료일</th>
            <th>메모</th>  
            <th>담당 계정</th>
            <th>오픈 계정</th>
            {(role === 'master' || role === 'admin') && <th>결제</th>}
            <th>수정</th>
          </tr>
        </thead>
        <tbody>
          {slots.map((slot) => (
            <tr key={slot.id}>
              <td>{slot.id}</td>
              <td>
                <div style={{
                  width: '10px',
                  height: '10px',
                  borderRadius: '50%',
                  backgroundColor: getColorIndicator(slot.slotErrorState, slot.startAt, slot.endAt),
                  display: 'inline-block'
                }}></div>
              </td>
              <td>{slot.workKeyword}</td>
              <td>{slot.rankKeyword}</td>
              <td>{slot.mid}</td>
              <td>{slot.originMid}</td>
              <td>{slot.startAt}</td>
              <td>{slot.endAt}</td>
              <td>{slot.description}</td>
              <td>{slot.adminId}</td>
              <td>{slot.ownerId}</td>
              {(role === 'master' || role === 'admin') && <td>{getPaymentStateText(slot.slotPaymentState === 'Y' ? 'y' : 'n')}</td>}
              <td>
                {isDateInRange(slot.startAt, slot.endAt) ? (
                    <button
                      onClick={() => openModal(slot)} 
                      style={{fontSize:'12px', padding:'3px 5px',  backgroundColor:'#f7f7f7', borderRadius:'5px', borderColor:'grey'}}>
                      수정
                    </button>
                ) : (
                    '불가'
                )}
              </td>        
            </tr>
          ))}
        </tbody>
      </table>

      <Modal
        isOpen={modalIsOpen}
        onRequestClose={closeModal}
        contentLabel="Slot 수정"
        style={{
          overlay: {
            backgroundColor: 'rgba(0, 0, 0, 0.75)'
          },
          content: {
            width: '400px',
            height: 'max-content',
            margin: 'auto',
            border: '1px solid #ccc',
            background: '#fff',
            overflow: 'auto',
            WebkitOverflowScrolling: 'touch',
            borderRadius: '4px',
            outline: 'none',
            padding: '20px',
            display: 'flex',
            flexDirection: 'column'
          }
        }}
      >
          <h2 style={{ color: '#333', marginBottom: '20px' }}>Slot 수정</h2>        <form>
          <label style={{ display: 'flex', flexDirection: 'column', gap: '5px' }}>
            작업 키워드:
            <input
              type="text"
              name="workKeyword"
              value={formData.workKeyword}
              onChange={handleChange}
              style={{ padding: '10px', border: '1px solid #ccc', borderRadius: '4px' }}
            />
          </label>
          <br />
          <label style={{ display: 'flex', flexDirection: 'column', gap: '5px' }}>
            순위 키워드:
            <input
              type="text"
              name="rankKeyword"
              value={formData.rankKeyword}
              onChange={handleChange}
              style={{ padding: '10px', border: '1px solid #ccc', borderRadius: '4px' }}
            />
          </label>
          <br />
          <label style={{ display: 'flex', flexDirection: 'column', gap: '5px' }}>
            MID:
            <input
              type="text"
              name="mid"
              value={formData.mid}
              onChange={handleChange}
              style={{ padding: '10px', border: '1px solid #ccc', borderRadius: '4px' }}
            />
          </label>
          <br />
          <label style={{ display: 'flex', flexDirection: 'column', gap: '5px' }}>
            원부 MID:
            <input
              type="text"
              name="originMid"
              value={formData.originMid}
              onChange={handleChange}
              style={{ padding: '10px', border: '1px solid #ccc', borderRadius: '4px' }}
            />
          </label>
          <br />
          <label style={{ display: 'flex', flexDirection: 'column', gap: '5px' }}>
            메모:
            <input
              type="text"
              name="description"
              value={formData.description}
              onChange={handleChange}
              style={{ padding: '10px', border: '1px solid #ccc', borderRadius: '4px' }}
            />
          </label>
          <br />
           {(role === 'master') && (<label style={{ display: 'flex', flexDirection: 'column', gap: '5px' }}>
            결제 상태:
            <div style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
              <label style={{ display: 'flex', alignItems: 'center', gap: '5px' }}>
                <input
                  type="radio"
                  name="slotPaymentState"
                  value="y"
                  checked={formData.slotPaymentState === 'y'}
                  onChange={handleChange}
                  style={{ marginRight: '5px' }}
                />
                결제 완료
              </label>
              <label style={{ display: 'flex', alignItems: 'center', gap: '5px' }}>
                <input
                  type="radio"
                  name="slotPaymentState"
                  value="n"
                  checked={formData.slotPaymentState === 'n'}
                  onChange={handleChange}
                  style={{ marginRight: '5px' }}
                />
                미결제
              </label>
            </div>
          </label>)}
          <br />
          <div style={{ display: 'flex', justifyContent: 'center',  }}>
            <button 
              type="button" 
              onClick={handleSave}
              style={{ padding: '10px 20px', border: 'none', borderRadius: '4px', background: '#4CAF50', color: 'white', cursor: 'pointer', marginRight:'10px' }}>
              저장
            </button>
            <button 
              type="button" 
              onClick={closeModal} 
              style={{ padding: '10px 20px', border: 'none', borderRadius: '4px', background: 'grey', color: 'white', cursor: 'pointer', marginLeft:'10px' }}>
              취소
            </button>
            {role === 'master' && (
              <button 
                type="button" 
                onClick={() =>handleDelete(formData.id)} 
                style={{ padding: '10px 20px', border: 'none', borderRadius: '4px', background: '#f44336', color: 'white', cursor: 'pointer', marginLeft:'20px' }}>
                삭제
              </button>
            )}
          </div>
          
        </form>
      </Modal>
    </div>
  );
};

export default SlotTable;
