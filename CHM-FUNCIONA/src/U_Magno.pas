unit U_Magno;

interface

uses U_ClasseRotinasGenericas, ADODB, SysUtils, ActiveX,U_ClasseAnaliseHarmonicaSis, U_ClassePrevisaoSis;

type
  TMagno = class
  private
  public
     procedure GeraArqAlturasDll( CodEstacao : Word; CodEqpto : Word; DataIni, DataFim, Arquivo: string  );
     procedure GeraArqConstantesDll( CodEstacao: Word; CodAnalise: integer; Arquivo: string  );
     function getConnection() :  TADOConnection;
     function RetornaZ0Analise(Conexao: TADOConnection; CodAnalise: Integer): Real;
     procedure previsaoColunas( CodEstacao: Word; CodAnalise: Integer; vDataInicial, vDataFinal: TDateTime;
        Diretorio: string; TipoSaida : Word; ArqConst : string );
  end;

implementation


function TMagno.getConnection() : TADOConnection;
var
  AdoConnection : TADOConnection;
  StringConexao : string;
begin
  StringConexao := 'Provider=SQLOLEDB;User ID=joaquim;Data Source=10.5.112.42,1433/mare;Password=#2018_jo@quim_m@r&2;Initial Catalog=mare';

  Writeln('Conectando...');
  CoInitialize(nil);
  AdoConnection:=TADOConnection.Create( nil );
  AdoConnection.LoginPrompt:=False;
  AdoConnection.ConnectionString:=StringConexao;
  AdoConnection.Connected:=True;

  Writeln('Conectado!');

  Result := AdoConnection;
end;

function TMagno.RetornaZ0Analise(Conexao: TADOConnection; CodAnalise: Integer): Real;
var
  AdoConnection : TADOConnection;
  CRotGen: TClasseRotinasGenericas;
  Z0 : Real;
begin
  AdoConnection := getConnection();
  CRotGen := TClasseRotinasGenericas.Create();

  Z0 := CRotGen.RetornaZ0Analise(AdoConnection, CodAnalise );

  writeln('Z0 : ' + floattostr(Z0) );

  Result := Z0;
  
end;

procedure TMagno.GeraArqConstantesDll( CodEstacao: Word; CodAnalise: integer; Arquivo: string  );
var
  AdoConnection : TADOConnection;
  CRotGen: TClasseRotinasGenericas;
begin

  AdoConnection := getConnection();

  CRotGen := TClasseRotinasGenericas.Create();
  //CRotGen.GeraArqConstantesDll(AdoConnection, CodEstacao, CodAnalise, Arquivo);
  //CRotGen.GeraArquivoConstantesDll(AdoConnection, 1, CodAnalise, Arquivo);

  CRotGen.GeraArqConstantesPadraoDll( AdoConnection, CodEstacao, Arquivo );

end;  

procedure TMagno.previsaoColunas( CodEstacao: Word; CodAnalise: Integer; vDataInicial, vDataFinal: TDateTime;
    Diretorio: string; TipoSaida : Word; ArqConst : string );
var
  Prev : TClassePrevisaoSis;
  AdoConnection : TADOConnection;
  CRotGen: TClasseRotinasGenericas;
  Z0: Real;
begin
  Prev := TClassePrevisaoSis.create();
  AdoConnection := getConnection();
  Z0 := RetornaZ0Analise( AdoConnection, CodAnalise );
  Prev.PrevisaoColunasOld(AdoConnection, 1, CodEstacao, CodAnalise, vDataInicial, vDataFinal, Diretorio, Z0, TipoSaida, true, false, ArqConst);
end;


procedure TMagno.GeraArqAlturasDll( CodEstacao : Word; CodEqpto : Word; DataIni, DataFim, Arquivo: string  );
var
  AdoConnection : TADOConnection;
  CRotGen: TClasseRotinasGenericas;
  //Sis : TClasseAnaliseHarmonicaSis;
begin
  AdoConnection := getConnection();

  CRotGen := TClasseRotinasGenericas.Create();
  CRotGen.GeraArqAlturasDll(AdoConnection, CodEstacao, CodEqpto, DataIni, DataFim, Arquivo);

  //Sis := TClasseAnaliseHarmonicaSis.create();
  //Sis.geraArqAlturasDll( Arquivo );

end;

end.
