package net.mbs.ybma.models

class DrawerMenu {
    var mId: Long = 0
    var mImageURL: String? = null
    var mText: String? = null
    var mIconRes = 0

    constructor() {}

    constructor (
        mId: Long,
        mImageURL: String?,
        mText: String?,
        mIconRes: Int
    ) {
        this.mId = mId
        this.mImageURL = mImageURL
        this.mText = mText
        this.mIconRes = mIconRes
    }


}