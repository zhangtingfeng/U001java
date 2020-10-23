import path from "path"
export default function (name) {
    return path.join(path.resolve(__dirname), "../../", name)
}