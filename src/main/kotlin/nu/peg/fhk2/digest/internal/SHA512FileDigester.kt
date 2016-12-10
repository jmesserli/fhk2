package nu.peg.fhk2.digest.internal

import org.bouncycastle.crypto.digests.SHA512Digest

/**
 * joel @10.12.2016
 */
class SHA512FileDigester : GeneralFileDigester(SHA512Digest(), 1024)