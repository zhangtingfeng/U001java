let AUTHS = []
let USER = {}
export let user = {
    set(v) {
        USER = Object.assign({}, v)
    },
    get() {
        return USER
    }
}
export let auths = {
    set(v) {
        AUTHS = v
    },
    get() {
        return AUTHS
    },
    has(v) {
        return AUTHS && AUTHS.find(t => t === v)
    }
}
