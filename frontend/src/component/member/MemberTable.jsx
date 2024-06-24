import React from 'react';

const MemberTable = ({members}) => {

  return (
    <div>
      <table>
        <thead>
          <tr>
            <th>아이디</th>
            <th>생성 어드민</th>
            <th>총 진행 슬롯</th>
            <th>진행 중 슬롯</th>
            <th>종료된 슬롯</th>
            <th>메모</th>
            <th>가입일</th>
          </tr>
        </thead>
        <tbody>
          {members.map((member) => (
            <tr key={member.member}>
                <td>{member.member}</td>
              <td>{member.admin}</td>
              <td>{member.total}</td>
              <td>{member.running}</td>
              <td>{member.closed}</td>
              <td>{member.description}</td>
              <td>{member.createdAt}</td>
            </tr>
          ))
          }
        </tbody>
      </table>
    </div>
  );
};

export default MemberTable;
