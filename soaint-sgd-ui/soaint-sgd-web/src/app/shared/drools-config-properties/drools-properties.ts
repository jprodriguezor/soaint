import {tassign} from 'tassign';
export const droolsPayload = {
  'lookup': 'ksession-rules',
  'commands': [
    {
      'fire-all-rules': ''

    }
  ]
};

export class RulesServer {

  static requestPayload(payload) {
    return tassign(droolsPayload, {
      commands: [{
        insert: {
          'out-identifier': 'Medio',
          'return-object': true,
          'object': {
            'co.com.soaint.sgd.model.MedioRecepcion': {
              'codMedioRecepcion': payload
            }
          }
        }
      }, ...droolsPayload.commands]
    })
  }
}
