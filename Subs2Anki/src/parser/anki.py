import json
import urllib.request as urllib2
import sys


def run(action, **params):
    if(action=='createDeck'):
        createDeck(sys.argv[2])

    if(action=='createNote'):
        createNote(sys.argv[2], sys.argv[3], sys.argv[4], sys.argv[5])

    if(action=='getDecks'):
        getDecks()


def request(action, **params):
    return {'action': action, 'params': params, 'version': 6}


def invoke(action, **params):
    requestJson = json.dumps(request(action, **params)).encode('utf-8')
    response = json.load(urllib2.urlopen(urllib2.Request('http://localhost:8765', requestJson)))
    if len(response) != 2:
        raise Exception('response has an unexpected number of fields')
    if 'error' not in response:
        raise Exception('response is missing required error field')
    if 'result' not in response:
        raise Exception('response is missing required result field')
    if response['error'] is not None:
        raise Exception(response['error'])
    return response['result']


def createDeck(deckName):
    invoke('createDeck', deck=deckName)


def createNote(deckName, expression, meaning, reading):
    note = {'deckName': deckName,
            'modelName': 'Japanese (recognition&recall)',  # possibly add option for user choice
            'fields': {'Expression': expression, 'Meaning': meaning, 'Reading': expression + '[' + reading + ']'},
            'tags': ['tag1']}
    print(note)
    invoke('addNote', note=note)


def getDecks():
    result = invoke('deckNames')
    print('got list of decks: {}'.format(result))


run(sys.argv[1])
