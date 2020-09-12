import { Injectable } from '@angular/core';
import { Configuration } from '../models/configuration';
import {BehaviorSubject, Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ConfigurationService {
  private config: Configuration;
  private loaded = new BehaviorSubject<boolean>(false);

  constructor() {
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', this.loadConfiguration.bind(this));
    xhr.open('GET', '/config');
    xhr.send();
  }

  public getConfiguration(): Configuration {
    return this.config;
  }

  public get loaded$(): Observable<boolean> {
    return this.loaded;
  }

  private loadConfiguration(event: ProgressEvent<XMLHttpRequest>): void {
    if (event.target.status === 200) {
      this.config = JSON.parse(event.target.response);
    } else {
      this.config = {
        apiBaseUrl: '',
      };
    }
    this.loaded.next(true);
  }
}
