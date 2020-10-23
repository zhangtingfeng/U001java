let isDebug = true

export default function (v) {
    if (undefined === v) {
        return isDebug
    } else {
        isDebug = !!v
        console.log("Enter [" + (isDebug ? "debug" : "release") + "] mode")
    }
}