import fs from "fs"
import resolve from "./resolve"

function delFile(path, matcher) {
    if (!fs.existsSync(path))
        return

    if (fs.statSync(path).isDirectory()) {
        fs.readdirSync(path).forEach((f) => {
            delFile(`${path}/${f}`, matcher)
        })
    } else {
        if (!matcher || (matcher && matcher.test(path))) {
            fs.unlinkSync(path)
        }
    }
}
export default function (dist, matcher) {
    delFile(resolve(dist), matcher)
}