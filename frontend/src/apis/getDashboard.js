import axios from 'axios';

const getDashboard = async (accessToken) => {

    try {
        const serverUrl = process.env.REACT_APP_SERVER_URL;

        const response = await axios.get(`${serverUrl}/slots/dashboard`, {
            headers: {
                Authorization: accessToken,
            },
        });

        return response;

    } catch (error) {
        if (error.response.status === 401) {
        }
        throw error;
    }


};

export default getDashboard;