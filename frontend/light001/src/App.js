import React from 'react';
import {BrowserRouter, Routes, Route} from 'react-router-dom';
import Login from './component/login/Login'
import Dashboard from './component/dashboard/Dashboard'
import Slot from './component/slot/Slot';
import Member from './component/member/Member';

function App() {

  return (
    <BrowserRouter>
      <Routes>
        <Route path='/' element={<Login/>}/>
        <Route path='/dashboard' element={<Dashboard/>}/>
        <Route path='/slots' element={<Slot/>}/>
        <Route path='/members' element={<Member/>}/>
      </Routes>
    </BrowserRouter>      
  );
}

export default App;
