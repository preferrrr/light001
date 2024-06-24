import React, { useState } from 'react';
import {useNavigate} from 'react-router-dom';
import './Login.css';
import logo from './light.png'; 
import {useCookies} from 'react-cookie';
import login from '../../apis/login';

const Login = () => {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [refresh, setRefresh] = useCookies(['refresh']);
    const router = useNavigate();

    const handleLogin = async (event) => {
        event.preventDefault();
        
        login(username, password)
        .then((res) => {
            const accessToken = res.headers.authorization;
            const refreshToken = res.headers['cookie']; 

            setRefresh('refresh', refreshToken);
            localStorage.setItem('accessToken', accessToken);

            router('/dashboard');
        })
        .catch((error)=> {
            if (error.response.status === 400) {
                alert('아이디 혹은 비밀번호가 틀렸습니다');
            } else {
                alert('로그인 요청에 실패했습니다');
            }
        })
        
    };


    return (
        <div className="login-container">
            <form className="login-form" onSubmit={handleLogin}>

                <img src={logo} alt="Logo" />
                <h2>LOGIN</h2>

                <div className='inputWrap'>

                    <div className='idWrap'>
                        <label htmlFor="username">아이디</label>
                        <input
                            type="text"
                            id="username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>

                    <div className='passwordWrap'>
                        <label htmlFor="password">비밀번호</label>
                        <input
                            type="password"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>

                </div>

                <button type="submit">로그인</button>

            </form>
        </div>
    );
};

export default Login;
