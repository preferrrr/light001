import axios from 'axios';

const login = async (id, password) => {

    try {
      const serverUrl = process.env.REACT_APP_SERVER_URL;
      const response = await axios.post(`${serverUrl}/members/login`, {
        id: id,
        password: password
      });
      return response;
    } catch (error) {
      throw error;
    }
};

export default login;