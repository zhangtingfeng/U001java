import fs from "fs"

function copy(src, dst) {
    fs.readdirSync(src).forEach((path) => {
        let _src = `${src}/${path}`
        let _dst = `${dst}/${path}`
        fs.stat(_src, (err, stats) => {
            if (err) throw err;
            if (stats.isFile()) {
                let readable = fs.createReadStream(_src)
                let writable = fs.createWriteStream(_dst)
                readable.pipe(writable)
            } else if (stats.isDirectory()) {
                checkDirectory(_src, _dst, copy)
            }
        });
    });
}

function checkDirectory(src, dst, callback) {
    fs.access(dst, fs.constants.F_OK, (err) => {
        if (err) {
            fs.mkdirSync(dst);
            callback(src, dst);
        } else {
            callback(src, dst);
        }
    })
}

export default function (src, dist) {
    copy(src, dist)
}
