let isDebug = true

export default function (v) {
    if (undefined === v) {
        return isDebug
    } else {
        isDebug = !!v
        console.log("Set " + (isDebug ? "Debug" : "Release") + " Mode")
    }
}