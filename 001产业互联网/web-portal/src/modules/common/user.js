// Sys user
import $ from "jquery"
import ajax from '@c/ajax'
import { user, auths } from '@f/framework'
export default {
    user: {
        userid: '',
        sid: '',
        name: ''
    },
    menus: [],
    async init() {
        let data = await ajax.get({
            url: 'api/session',
            param: {
                menu: 1,
                auths: 1
            }
        })
        if (data.user) {
            $.extend(this.user, data.user)
            this.menus = data.menu || []
            user.set(data.user)
            auths.set(data.auths || [])
        }
        return this
    }
}