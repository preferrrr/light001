import React, {useState} from 'react';

const SearchBar = ({ onSearch }) => {
    
    const [type, setType] = useState('');
    const [keyword, setKeyword] = useState('');
  
    const handleSearch = () => {
      onSearch(type, keyword);
    };

    const handleKeyPress = (e) => {
      if (e.key === 'Enter') {
        handleSearch();
      }
    };

    return (
      <div className="search-bar">
        <select value={type} onChange={(e) => setType(e.target.value)}>
          <option value="" style={{fontSize:'12px'}}>유형 선택</option>
          <option value="mid" style={{fontSize:'12px'}}>MID</option>
          <option value="originMid" style={{fontSize:'12px'}}>원부 MID</option>
          <option value="id" style={{fontSize:'12px'}}>슬롯 번호</option>
          <option value="ownerId" style={{fontSize:'12px'}}>일반 계정</option>
          <option value="adminId" style={{fontSize:'12px'}}>담당 계정</option>
          <option value="workKeyword" style={{fontSize:'12px'}}>작업 키워드</option>
          <option value="rankKeyword" style={{fontSize:'12px'}}>순위 키워드</option>
          <option value="description" style={{fontSize:'12px'}}>메모</option>
        </select>
        <input
          className='searchInput'
          type="text"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          onKeyDown={handleKeyPress}
          placeholder="검색어 입력"
        />
        <button className='searchBtn' onClick={handleSearch}>검색</button>
      </div>
    );
};

export default SearchBar;