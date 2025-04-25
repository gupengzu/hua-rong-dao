import request from '@/utils/request'

//发送验证码
export const sendVerificationCode = (email) => {
    return request.get(`/registry?email=${email}`);
} 

export const registerApi = (data) => {
    return request.post('/registry', data);
}