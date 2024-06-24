import axios from 'axios';

export const signup = async (username, password, description, accessToken) => {

    try {
        const serverUrl = process.env.REACT_APP_SERVER_URL;

        const response = await axios.post(`${serverUrl}/members`,{
            id: username,
            password: password,
            description: description,
        }, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: accessToken,
            },
        });
    
        return response;

    } catch(error) {
        throw error;
    }


};
