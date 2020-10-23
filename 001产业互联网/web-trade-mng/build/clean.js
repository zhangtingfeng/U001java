import {
    clean
} from "./utils"

let reserve;

if (process.argv.length > 2){
    reserve = new RegExp(process.argv[2])
}

clean('dist', reserve)

