import axios from 'axios';

export const getMembers = async (currentPage, accessToken) => {
    try {
        const serverUrl = process.env.REACT_APP_SERVER_URL;

        const response = await axios.get(`${serverUrl}/members`, {
            headers: {
                Authorization: accessToken,
            },
            params: {page: currentPage}
        });
        
        return response;
    } catch (error) {
        throw error;
    }

};
