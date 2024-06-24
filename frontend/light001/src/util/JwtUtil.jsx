import base64 from 'base-64';

const parseToken = (token) => {
  try {
    const actualToken = token.slice(7);
    const payload = actualToken.split('.')[1];
    const decodedPayload = atob(payload);
    const parsedPayload = JSON.parse(decodedPayload);
    const role = parsedPayload.role;
    return role;
  } catch (error) {
    console.error('Failed to parse token:', error);
    return null;
  }
};

export default parseToken;