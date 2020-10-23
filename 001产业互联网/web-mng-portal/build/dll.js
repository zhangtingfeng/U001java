import {
    fileExists,
    buildName,
    resolve
} from "./utils"
import compile from "./compile"

export default (name) => {
    return new Promise((s, f) => {
        const vendorManifest = resolve(`dist/${buildName()}/${name}/manifest_${name}.json`)
        if (fileExists(vendorManifest)) {
            s(`${name} is complete.\n`)
        } else {
            const config = require(`./config/${name}.config`)
            compile(config.default).then(s, f)
        }
    })
}