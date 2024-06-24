import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Dashboard.css'
import Left from '../left/Left'
import getDashboard from '../../apis/getDashboard';
import parseToken from '../../util/JwtUtil';

const Dashboard = () => {

    const [dashboardData, setDashboardData] = useState(); 
    const router = useNavigate();
    const accessToken = localStorage.getItem('accessToken');
    const role = parseToken(accessToken);
    
    useEffect(() => {
        getDashboard(accessToken)
        .then((res) => {
            setDashboardData(res.data.data);
        })
        .catch((error) => {
            alert(error.response.data.message);
  
            if (error.response.status === 401 || error.response.status === 403) {
                localStorage.removeItem('accessToken');
                router('/');
                return;
            }
        })

    }, []);

    if (!dashboardData) {
        return <div className="loading-container">Loading...</div>; 
    }

    return (
        <div className="container-dashboard">

            <div className='left-panel'>
                <Left role = {role}/>
            </div>

            <div className="dashboard-right">

                <div className='boxes'>
                    <div className="box1">
                        <div className='box-head'></div>
                        <div className="box-title">전체 슬롯</div>
                        <div className="box-number"> {dashboardData.total} </div>
                    </div>

                    <div className="box1">
                        <div className='box-head'></div>
                        <div className="box-title">대기 슬롯</div>
                        <div className="box-number"> {dashboardData.waiting} </div>
                    </div>

                    <div className="box1">
                        <div className='box-head'></div>
                        <div className="box-title">구동 슬롯</div>
                        <div className="box-number"> {dashboardData.running} </div>
                    </div>

                    <div className="box2">
                        <div className='box-head'></div>
                        <div className="box-title">만료 예정 슬롯</div>
                        <div className="box-number">{dashboardData.expiring}</div>
                    </div>

                    <div className="box2">
                        <div className='box-head'></div>
                        <div className="box-title">종료 슬롯</div>
                        <div className="box-number">{dashboardData.closed}</div>
                    </div>

                    <div className="box2">
                        <div className='box-head'></div>
                        <div className="box-title">오류 슬롯</div>
                        <div className="box-number">{dashboardData.error}</div>
                    </div>

                </div>
            </div>
        </div>
    );
};

export default Dashboard;
