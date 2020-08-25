import {Injectable} from '@angular/core';
import {Configuration} from '../models/configuration';

@Injectable({
  providedIn: 'root',
})
export class ConfigurationService {
  constructor() {
  }

  public getConfiguration(): Configuration {
    return {
      apiBaseUrl: "http://localhost:8080/"
    }
  }
}
