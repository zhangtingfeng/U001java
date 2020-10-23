let APPS = {}

export default {
    get(name, app) {
        return APPS[name][app]
    },

    set(name, apps) {
        APPS[name] = apps
    }
}