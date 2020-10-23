import debug from "./debug"

export default function () {
    let name = (debug() ? "debug" : "release")
    return name
}