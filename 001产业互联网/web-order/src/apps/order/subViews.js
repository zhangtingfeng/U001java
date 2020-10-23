import base from "./sub/base"
import goods from "./sub/goods"
import dealer from "./sub/dealer"
import contract from "./sub/contract"

import payreq from "./sub/payreq"
import payreqRecord from "./sub/payreqRecord"
import orderrqSend from "./sub/orderrqSend"
import orderrqTake from "./sub/orderrqTake"
import payreqRecordlist from "./sub/payreqRecordList"
import sign from "./sub/signvue"

import evaluate from "./sub/evaluate"

export default {
    base: base,
    goods: goods,
    buyer: dealer,
    seller: dealer,
    contract: contract,
    pay_req: payreq,
    pay: payreqRecord,
    delv: orderrqSend,
    recep: orderrqTake,
    pay_list: payreqRecordlist,
    sign: sign,
    evaluate: evaluate
}

