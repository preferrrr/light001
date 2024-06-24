import React, { useEffect, useState } from 'react';
import Left from '../left/Left';
import MemberTable from '../member/MemberTable';
import SignUpForm from './SignUpForm';
import {getMembers} from '../../apis/getMembers';
import './Member.css';
import Pagination from '../pagination/Pagination';
import CreateSlot from './CreateSlot';
import { useNavigate } from 'react-router-dom';
import parseToken from '../../util/JwtUtil';


function Member() {

    const router = useNavigate();
    const [members, setMembers] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const accessToken = localStorage.getItem('accessToken');
    const role = parseToken(accessToken);

    useEffect(() => {

        getMembers(currentPage, accessToken)
        .then((res) => {
            setMembers(res.data.data.content);
            setTotalPages(res.data.data.totalPages);
        })
        .catch((error) => {
            if (error.response.status === 401 || error.response.status === 403) {
                router('/');
                return;
            }
            alert(error.response.data.message);
        })
    
      }, [currentPage]);

    return (
        <div className='container'>
            <div className='left-panel'>
                <Left role = {role}/>
            </div>
            <div className='right-member'>
                <div className='signup-wrap'>
                    <SignUpForm accessToken={accessToken}/>
                    {role === 'admin' && <CreateSlot accessToken={accessToken}/>}
                </div>
                <div className='members-wrap'>
                    <MemberTable members={members}/>
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

export default Member;
