let VIEWS = {}

export default {
    get(name = undefined) {
        return name ? VIEWS[name] : VIEWS
    },

    set(name, comp) {
        if (comp.constructor && "Promise" === comp.constructor.name) {
            VIEWS[name] = (resolve) => {
                comp.then(d => resolve(d), e => resolve({
                    template: name
                }))
            }
        } else if (comp.__esModule && comp.default) {
            VIEWS[name] = comp.default
        } else
            VIEWS[name] = comp
    }
}