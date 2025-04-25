import axios from 'axios'

//创建axios实例对象
const request = axios.create({
  baseURL: '/api',
  timeout: 600000
})

//axios的响应 response 拦截器
request.interceptors.response.use(
  (response) => { //成功回调
    return response.data
  },
  (error) => { //失败回调
    return Promise.reject(error)
  }
)

//axios的请求 request 拦截器(主要是获取localStorage的token，在请求头中添加token)
request.interceptors.request.use(
  (config) => {//成功回调
    const loginUser = JSON.parse(localStorage.getItem('loginUser'));
    if (loginUser && loginUser.token) {
      config.headers.token = loginUser.token;
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

export default request