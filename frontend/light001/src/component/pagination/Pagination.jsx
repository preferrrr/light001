import React from 'react';
import './Pagination.css';

const Pagination = ({ currentPage, totalPages, onPageChange }) => {
    // UI에 보여지는 현재 페이지 번호 (1부터 시작)
    const currentPageDisplay = currentPage + 1;

    // 전체 페이지 숫자를 배열로 생성
    const pageNumbers = Array.from({ length: totalPages }, (_, index) => index + 1);

    // 페이지 숫자를 10개씩 묶어서 표시하기 위한 배열
    const rows = [];
    for (let i = 0; i < pageNumbers.length; i += 10) {
        rows.push(pageNumbers.slice(i, i + 10));
    }

    // 페이지 번호를 클릭할 때 호출되는 함수
    const handleClickPage = (pageNumber) => {
        onPageChange(pageNumber - 1); // 실제 사용할 currentPage 값 (0부터 시작)을 전달
    };

    return (
        <div className="pagination">
            {rows.map((row, rowIndex) => (
                <div key={rowIndex} className="pagination-row">
                    {row.map((pageNumber) => (
                        <span
                            key={pageNumber}
                            className={pageNumber === currentPageDisplay ? 'current-page' : ''}
                            onClick={() => handleClickPage(pageNumber)}
                        >
                            {pageNumber}
                        </span>
                    ))}
                </div>
            ))}
        </div>
    );
};

export default Pagination;
