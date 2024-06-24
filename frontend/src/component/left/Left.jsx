import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Left.css'
import logo from './light.png'; 

const Left = ({role}) => {

    const router = useNavigate();

    const toSlots = () => {
        router('/slots');
    };

    const toDashboard = () => {
        router('/dashboard');
    };

    const toMembers = () => {
        router('/members');
    }

    return (
    <div className="side">

        <img src={logo} alt="Logo" />
        <span> LIGHT</span> 

        <button className="btn" onClick={toDashboard}><p>대시보드</p></button>
        <button className="btn" onClick={toSlots}><p>슬롯 관리</p></button>
        { (role === 'master' || role === 'admin') && (
                <button className="btn" onClick={toMembers}><p>회원 관리</p></button>
         )}

        
    </div>
    );
}

export default Left