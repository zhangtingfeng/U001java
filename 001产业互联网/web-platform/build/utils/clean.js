import fs from "fs"
import resolve from "./resolve"

function delFoldAndFile(path, reserve) {
    if (reserve && reserve.test(path)) {
        return false
    }

    if (!fs.existsSync(path))
        return true

    process.stdout.write(`rm ${path}\n`)
    if (fs.statSync(path).isDirectory()) {
        let empty = true;
        fs.readdirSync(path).forEach((f) => {
            empty &= delFoldAndFile(`${path}/${f}`, reserve)
        })
        if (empty) {
            fs.rmdirSync(path);
            return true;
        } else {
            return false;
        }
    } else {
        fs.unlinkSync(path)
        return true
    }
}

export default function(dist, reserve) {
    delFoldAndFile(resolve(dist), reserve)
}