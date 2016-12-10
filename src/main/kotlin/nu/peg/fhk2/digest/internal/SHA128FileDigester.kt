package nu.peg.fhk2.digest.internal

import org.bouncycastle.crypto.digests.SHA1Digest

class SHA128FileDigester : GeneralFileDigester(SHA1Digest(), 512)