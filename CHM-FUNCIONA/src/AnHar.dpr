program AnHar;

{$APPTYPE CONSOLE}

uses
  SysUtils,
  BbtFuncoes in 'BbtFuncoes.pas',
  BNDO in 'BNDO.pas',
  U_Dll in 'U_Dll.pas',
  U_Tipos in 'U_Tipos.pas',
  U_ClasseAnaliseHarmonicaSis in 'U_ClasseAnaliseHarmonicaSis.pas',
  U_ClasseRotinasGenericas in 'U_ClasseRotinasGenericas.pas',
  U_ClasseDirecionador in 'U_ClasseDirecionador.pas',
  U_ClassePrevisaoSis in 'U_ClassePrevisaoSis.pas';

var
  Dir: TDirecionador;
begin
  { TODO -oUser -cConsole Main : Insert code here }
  DecimalSeparator:= '.';
  //Writeln(ParamCount);
  //Writeln(ParamStr(1));
  Dir:= TDirecionador.Create;
  if (ParamStr(1) = '1') then
    Dir.analiseHarmonica(ParamStr(2), ParamStr(3), ParamStr(4))
  else
    Dir.previsao(ParamStr(2), ParamStr(3), StrToDate(ParamStr(4)),
      StrToDate(ParamStr(5)), StrToFloat(ParamStr(6)));
end.