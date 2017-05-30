import { SoaintSgdWebPage } from './app.po';

describe('soaint-sgd-web App', () => {
  let page: SoaintSgdWebPage;

  beforeEach(() => {
    page = new SoaintSgdWebPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
