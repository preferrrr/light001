import React, { useEffect, useState } from 'react';
import './Slot.css';
import Left from '../left/Left'
import Pagination from '../pagination/Pagination'; 
import SlotTable from './SlotTable';
import SearchBar from './SearchBar';
import {getSlots} from '../../apis/getSlots'
import { useNavigate } from 'react-router-dom';
import parseToken from '../../util/JwtUtil';

function Slot() {
  
  const router = useNavigate();
  const [slots, setSlots] = useState([]);
  const [searchParams, setSearchParams] = useState({ type: '', value: '' });
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const accessToken = localStorage.getItem('accessToken');
  const role = parseToken(accessToken);  
  useEffect(() => {


    getSlots(searchParams.type, searchParams.value, currentPage, accessToken).then((res) => {
      setSlots(res.data.data.content);
      setTotalPages(res.data.data.totalPages);
    })
    .catch((error) => {
      if (error.response.status === 401 || error.response.status === 403) {
        router('/');
        return;
      }
      alert(error.response.data.message);
    });


  }, [searchParams, currentPage]);

  const handleSearch = (type, keyword) => {
    setSearchParams({ type, value: keyword }); 
    setCurrentPage(0); 
  };

  return (
    <div className="container">

      <div className='left-panel'>
          <Left role = {role}/>
      </div>

      <div className='right-slot'>

        <div className='search'>
            <SearchBar onSearch={handleSearch} />
        </div>

        <div className='slots'>
            <SlotTable slots={slots} accessToken={accessToken} role={role} />
        </div>

        <div className='paginationWrap'>
          <Pagination
              currentPage={currentPage}
              totalPages={totalPages}
              onPageChange={setCurrentPage}
          />
        </div>

      </div>

    </div>
  );
}


export default Slot;
