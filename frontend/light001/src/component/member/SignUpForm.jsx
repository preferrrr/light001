import React, { useState, useContext } from 'react';
import { signup } from '../../apis/signup';
import { useNavigate } from 'react-router-dom';

const SignUpForm = ({accessToken}) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [description, setDescription] = useState('');
  const [isPasswordMatch, setIsPasswordMatch] = useState(false);
  const router = useNavigate();

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
    setIsPasswordMatch(e.target.value === confirmPassword);
  };

  const handleConfirmPasswordChange = (e) => {
    setConfirmPassword(e.target.value);
    setIsPasswordMatch(e.target.value === password);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (isPasswordMatch) {
      signup(username, password, description, accessToken)
      .then(() => {
        setUsername('');
        setPassword('');
        setConfirmPassword('');
        setDescription('');
        alert('회원 등록에 성공했습니다.');
      })
      .catch((error) => {
        if (error.response.status === 401 || error.response.status === 403) {
          router('/');
          return;
      }
        alert(error.response.data.message);
      });
    }
  };

  return (
    <div className="signup">
      <form className="signup-form" onSubmit={handleSubmit}>
        <h2>회원 등록</h2>
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
              onChange={handlePasswordChange}
              required
            />
          </div>
          <div className='passwordWrap'>
            <label htmlFor="confirmPassword">비밀번호 확인</label>
            <input
              type="password"
              id="confirmPassword"
              value={confirmPassword}
              onChange={handleConfirmPasswordChange}
              required
              placeholder='비밀번호가 일치해야 버튼이 활성화됩니다.'
            />
          </div>

          <div className='passwordWrap'>
            <label htmlFor="confirmPassword">메모</label>
            <input
              type="text"
              id="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              required
            />
          </div>

        </div>
        <button type="submit" disabled={!isPasswordMatch} className={isPasswordMatch ? '' : 'disabled'}>
          회원가입
        </button>
      </form>
    </div>
  );
};

export default SignUpForm;
