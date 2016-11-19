package nu.peg.fhk2.util.internal

import org.bouncycastle.crypto.digests.SHA256Digest

/**
 * By joel @ 19.11.2016
 */
class SHA256FileDigester : GeneralFileDigester(SHA256Digest(), 512)