import { ajax } from "@f/vendor"
export default {
  user: {
    userid: '',
    sid: '',
    name: ''
  },
  async init() {
    let data = await ajax.get({ url: 'api/session' })
    if (data.user) {
      this.user = data.user
    }
    return this
  },
  logout() {
    if (this.user && this.user.userid) {
      this.user.userid = this.user.sid = this.user.name = ""
      ajax.delete({ url: "api/session" })
      window.location="login.html";
    }
  }
}
