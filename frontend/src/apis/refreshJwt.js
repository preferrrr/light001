import axios from 'axios';

const refreshJwt = async (refresh) => {

    axios.defaults.withCredentials=true;

    try {
      const serverUrl = process.env.REACT_APP_SERVER_URL;

        const response = await axios.get(`${serverUrl}/members/reissue`, {
            headers: {
                cookie: refresh
            },
        });
        
        return response;

      } catch (error) {
        alert(error.response.data.messsage);

        console.log('in refreshJwt errors');
        
        throw error;
      }

};

export default refreshJwt;