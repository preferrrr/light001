import axios from 'axios';

export const deleteSlot = async (id, accessToken) => {
    try {
        const serverUrl = process.env.REACT_APP_SERVER_URL;

        const response = await axios.delete(`${serverUrl}/slots`, {
            headers: {
                Authorization: accessToken,
            },
            params: {id: id}
        });
        
        return response;
    } catch (error) {
        throw error;
    }

};
