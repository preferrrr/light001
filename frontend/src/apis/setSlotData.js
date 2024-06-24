import axios from 'axios';

export const setSlotData = async (data, accessToken) => {

    try {
        const serverUrl = process.env.REACT_APP_SERVER_URL;

        const response = await axios.patch(`${serverUrl}/slots`,{
            id: data.id, 
            mid: data.mid, 
            originMid: data.originMid, 
            workKeyword: data.workKeyword, 
            rankKeyword: data.rankKeyword,
            description: data.description,
            slotPaymentState: data.slotPaymentState
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
