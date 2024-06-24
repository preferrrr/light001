import axios from 'axios';

export const getSlots = async (type, value, currentPage, accessToken) => {
    try {
        const serverUrl = process.env.REACT_APP_SERVER_URL;

        const params = {page: currentPage};

        if (type !== '' && value !== '') {
            params.type = type;
            params.value = value;
        }

        const response = await axios.get(`${serverUrl}/slots`, {
            headers: {
                Authorization: accessToken,
            },
            params: params,
        });

        return response;

    } catch (error) {
        throw error;
    }

};
