unit Unit1;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs,Idhttp;
             
type
  TForm1 = class(TForm)
    procedure FormCreate(Sender: TObject);
  private
    { Private declarations }
  public
    { Public declarations }
  end;

var
  Form1: TForm1;

implementation

{$R *.dfm}

procedure TForm1.FormCreate(Sender: TObject);
var
  UpUrl: string;
  UpFileName: string;
  FHttp: Tidhttp;
  MutPartForm: TIdMultiPartFormDataStream;
begin
     UpUrl:='http://127.0.0.1/upfile/upfile.asp';
     UpFileName:='C:\Documents and Settings\Administrator\桌面\test\web.mdb';
//const UpUrl = 'http://127.0.0.1/upfile/upfile.asp';
//const UpFileName = 'C:\Documents and Settings\Administrator\桌面\test\web.mdb';
  FHttp := Tidhttp.Create(nil);
    FHttp.HandleRedirects := true;
    FHttp.AllowCookies := true;

    MutPartForm := TIdMultiPartFormDataStream.Create;
    MutPartForm.AddFormField('act', 'upload');
    MutPartForm.AddFormField('upcount', '1');
    MutPartForm.AddFormField('filepath', 'data');
    MutPartForm.AddFormField('file1', 'filename="' + UpFileName + '"');
    MutPartForm.AddFormField('Submit', 'Submit');
    MutPartForm.AddFile('file1', UpFileName, 'text/plain');
    try
        response := FHttp.Post(UpUrl, MutPartForm);
    //Messagebox(0, PAnsiChar(response), 'ca', MB_OK);
    finally
        MutPartForm.Free;
        FHttp.Free;
    end;
  MessageBox(0,'不同意','提示',MB_OK);

  
end;

end.
