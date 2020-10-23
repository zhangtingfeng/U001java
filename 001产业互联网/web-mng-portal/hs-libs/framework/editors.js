let EDITORS = {}

export default {
    get(name = undefined) {
        return name ? EDITORS[name] : EDITORS
    },

    set(name, comp) {
        if (comp.constructor && "Promise" === comp.constructor.name) {
            EDITORS[name] = (resolve) => {
                comp.then(d => resolve(d), e => resolve({
                    template: name
                }))
            }
        } else if (comp.__esModule && comp.default) {
            EDITORS[name] = comp.default
        } else {
            EDITORS[name] = comp
        }
    }
}