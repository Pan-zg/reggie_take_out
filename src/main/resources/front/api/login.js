function loginApi(data) {
    return $axios({
      'url': '/user/login',
      'method': 'post',
      data
    })
  }

function loginoutApi() {
  return $axios({
    'url': '/user/loginout',
    'method': 'post',
  })
}

// 该方法用于实现短信验证码的发送（源代码缺失，自行补充）
function sendMsgApi(data) {
  return $axios({
    'url': '/user/sendMsg',
    'method': 'post',
    data
  })
}

  