import axios from 'axios';

export const createSlot = async (username, count, accessToken) => {
    const serverUrl = process.env.REACT_APP_SERVER_URL;
    try {

        const response = await axios.post(`${serverUrl}/slots`, {
            memberId: username,
            day: 9,
            count: count
        }, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: accessToken,
            },
        });

        return response;

    } catch (error) {
        throw error;
    }

};