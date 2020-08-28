import { Injectable } from '@angular/core';
import { Configuration } from '../models/configuration';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ConfigurationService {
  constructor() {}

  public getConfiguration(): Configuration {
    return {
      apiBaseUrl: environment.apiBaseUrl,
    };
  }
}
