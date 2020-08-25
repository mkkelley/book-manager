export class AuthenticationResponse {
  public authenticated: boolean;
  public authorities: string[];
  public credentials;
  public name: string;
  public principal: AuthenticationPrincipal;
}

export class AuthenticationPrincipal {
  public username: string;
  public enabled: boolean;
  public accountNonLocked: boolean;
  public accountNonExpired: boolean;
}
