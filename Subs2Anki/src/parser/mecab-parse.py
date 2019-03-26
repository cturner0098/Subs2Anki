import MeCab
import sys

mecab = MeCab.Tagger()
print(mecab.parse(sys.argv[1]))
