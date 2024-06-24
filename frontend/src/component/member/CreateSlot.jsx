import React, { useContext, useState } from 'react';
import { createSlot } from '../../apis/createSlot';
import { useNavigate } from 'react-router-dom';

const CreateSlot = ({accessToken}) => {
  const [username, setUsername] = useState('');
  const [count, setCount] = useState(0);
  const router = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    createSlot(username, count, accessToken)
    .then(() => {
      setUsername('');
      setCount(0);
      alert('슬롯 생성에 성공했습니다.');
    })
    .catch((error) => {
      alert(error.response.data.message);
      if (error.response.status === 401 || error.response.status === 403) {
        router('/');
        return;
      }
      alert(error.response.data.message);
    });

  };

  return (
    <div className="signup">
      <form className="signup-form" onSubmit={handleSubmit}>
        <h2>슬롯 생성</h2>
        <div className='inputWrap'>
          <div className='idWrap'>
            <label htmlFor="username">생성해줄 아이디</label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </div>
          <div className='passwordWrap'>
            <label htmlFor="count">개수</label>
            <input
              type="number"
              id="count"
              value={count}
              onChange={(e) => setCount(e.target.value)}
              required
            />
          </div>

        </div>
        <button type="submit" >
          생성
        </button>
      </form>
    </div>
  );
};

export default CreateSlot;
