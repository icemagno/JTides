unit U_ClasseRotinasGenericas;

interface

uses SysUtils, BNDO, Classes, DB, ADODB, U_Tipos,
  Controls, Math, StdCtrls, DateUtils;

type
  TClasseRotinasGenericas = class
  private
  public
    procedure GeraArqAlturasDll(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs; DataIni, DataFim, Arquivo: string);
    procedure GeraArqAlturasTempDll(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs; DataIni, DataFim, Arquivo: string);
    function RetornaNumeroEstacao(Conexao: TADOConnection; CodEstacao: PeqInteiroSs): LongoInteiroSs;
    function ValidaPeriodoAlturasTempDll(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs;
      DataIni, DataFim: string): Boolean;
    procedure GeraArquivoAlturasEquipamento(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs; DataIni,
      DataFim, Arquivo: string; Filtro: MinInteiroSs);
    function ValidaPeriodoAlturasDll(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs;
      DataIni, DataFim: string): Boolean;
    function ValidaPeriodoAlturasEquipamento(Conexao: TADOConnection; Area: MinInteiroSs; CodEquipEstacao: PeqInteiroSs;
      DataIni, DataFim: TDate): MinInteiroSs;
    procedure GeraArqConstantesDll(Conexao: TADOConnection; CodEstacao: PeqInteiroSs; CodAnalise: integer; Arquivo: string);
    function FormataIndiceComponente(Indice: string): string;
    procedure ConcatenaArquivos(ArqPrin, ArqDados: string; Ignorar: PeqInteiroSs; DelArqDados: Boolean);
    function ExisteReducao(Conexao: TADOConnection; CodEstacao: PeqInteiroSs; CodAnalise: LongoInteiroSs): Boolean;
    function RetornaCodigoEstacao(Conexao: TADOConnection; NumEstacao: LongoInteiroSs): PeqInteiroSs;
    function RetornaNomeEquipamento(Conexao: TADOConnection; CodEquipEstacao: PeqInteiroSs): string;
    function RetornaNomeEstacao(Conexao: TADOConnection; CodEstacao: PeqInteiroSs): string;
    procedure GeraArqConstantesPadraoDll(Conexao: TADOConnection; CodEstacao: PeqInteiroSs; Arquivo: string);
    function RetornaCodigoAnalisePeriodoPadrao(Conexao: TADOConnection; CodEstacao: PeqInteiroSs): Integer;
    function RetornaZ0Analise(Conexao: TADOConnection; CodAnalise: Integer): Real;
    function RetornaQtdAlturasObsHorasCheias(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs; DataIni, DataFim: string): Integer;
    function ExisteReducaoTemp(Conexao: TADOConnection; CodEstacao: PeqInteiroSs; CodAnalise: LongoInteiroSs): Boolean;
    function ExisteAlturasPeriodoTemp(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs; DataIni, DataFim: string): Boolean;
    procedure GeraArquivoAlturasTempEquipamento(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs; DataInicial,
      DataFinal: TDate; Arquivo: string; Filtro: Boolean);
    function ValidaPeriodoAlturasTempEquipamento(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: integer;
      DataIni, DataFim: string): Boolean;
    procedure ExcluiPeriodoAlturasTemp(Conexao: TADOConnection; CodEquipEstacao: PeqInteiroSs;
      DataIni, DataFim: string);
    function ExisteAlturasPeriodo(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs; DataIni, DataFim: string): Boolean;
    function RetornaNumeroEstacaoAnalise(Conexao: TADOConnection; Area: MinInteiroSs; CodAnalise: Integer): LongoInteiroSs;
    function RetornaCodigoEstacaoAnalise(Conexao: TADOConnection; Area: MinInteiroSs; CodAnalise: Integer): PeqInteiroSs;
    function RetornaQtdAlturasUnificadasDll(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs;
      DataIni, DataFim: string): Integer;
    function RetornaCodigoAnalisePeriodoPadraoUnificada(Conexao: TADOConnection; CodEstacao: PeqInteiroSs): Integer;
    function RetornaZ0AnaliseUnificada(Conexao: TADOConnection; Area: MinInteiroSs; CodAnalise: Integer): Real;
    function ValidaPeriodoAlturasUnificadasDll(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs;
      DataIni, DataFim: string; Area: MinInteiroSs): Boolean;
    function TransfereAlturasTemporarias(Conexao: TADOConnection; CodEquipEstacao: PeqInteiroSs;
      DataIni, DataFim: string): Smallint;
    function RetornaQtdAlturasUnificadas(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs;
      DataIni, DataFim: string): Integer;
    function RetornaCodigoAnaliseReducaoPadrao(Conexao: TADOConnection; CodEstacao: PeqInteiroSs): Integer;
    function TransfereAlturasPrincipais(Conexao: TADOConnection; CodEquipEstacao: PeqInteiroSs;
      DataIni, DataFim: string): Smallint;
    function RetornaCodigoEstacaoEquipEstacao(Conexao: TADOConnection; CodEquipEstacao: PeqInteiroSs): PeqInteiroSs;
    function RetornaCodigoEquipEstacaoAnalise(Conexao: TADOConnection; CodAnalise: LongoInteiroSs): PeqInteiroSs;
    function RetornaTaxaAmostragemEquipamento(Conexao: TADOConnection; CodEquipEstacao: PeqInteiroSs): MinInteiroSs;

    {*** ROTINAS CONSTANTES HARM�NICAS ***}
    function ExistePeriodoConstantes(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs;
      DataIni, DataFim: string): Boolean;
    function ExistePeriodoConstantesTemp(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs;
      DataIni, DataFim: string): Boolean;
    procedure GeraArquivoConstantesDll(Conexao: TADOConnection; Area: MinInteiroSs; CodAnalise: Integer; Arquivo: string);
    function EstacaoComPeriodoPadrao(Conexao: TADOConnection; CodEstacao: PeqInteiroSs): Boolean;
    {*************************************}

    function IdentaDataHora(DataHora: TDateTime; QtdMinutos: PeqInteiroSs): TDateTime;
    function RetornaCodigoPorto(Conexao: TADOConnection; CodEstacao: PeqInteiroSs): PeqInteiroSs;
    function RetornaDiferencaPeriodo(DataInicial, DataFinal: TDateTime; TipoRetorno: MinInteiroSs): Integer;
    procedure InterpolacaoSpline20(ITempo, DeltaT: MinInteiroSs; ArqDados, ArqSaida: string);
    function ExisteTabuaMares(Conexao: TADOConnection; CodPorto, Ano: PeqInteiroSs): Boolean;
    function RetornaParametroString(Linha: string; Separador: Char): string;
    function DataPosterior(Dia, DiaPosterior: TDate): Boolean;
    function ValidaDataString(Data: string): Boolean;
    procedure ListaEquipamentos(Conexao: TADOConnection; CodEstacao: PeqInteiroSs; var ComboBox: TCombobox);
    function FormataEspacosString(St: string; QtdEspacos: PeqInteiroSs; Direita: Boolean): string;
    function ApagaArquivoSeExiste(Arquivo: string): Boolean;
    procedure InsereDataHoraDados(DataInicial: TDateTime; ArquivoDados, ArquivoSaida: string;
      TaxaMinutos: MinInteiroSs);
    function SubtraiDataHora(DataHora: TDateTime; QtdMinutos: PeqInteiroSs): TDateTime;
    function ExisteReducaoPadrao(Conexao: TADOConnection; CodEstacao: PeqInteiroSs): Boolean;
    function IdentaDataHoraSegundos(DataHora: TDateTime; QtdSegundos: PeqInteiroSs): TDateTime;
    function RetornaQtdAlturasEquipamento(Conexao: TADOConnection; CodEquipEstacao: PeqInteiroSs;
      DataInicial, DataFinal: TDate): LongoInteiroSs;
    function RetornaQtdAlturasPeriodo(DataInicial, DataFinal: TDateTime;
      TaxaAmostragem: TAmostragem): Integer;
    function ValidaDataHoraAmostragem(DataHora: TDateTime;
      TaxaAmostragem: TAmostragem): Boolean;
    function ValidaPeriodoAlturasPorEquipamento(Conexao: TADOConnection; Area: TArea;
      CodEquipEstacao: TCodEquipEstacao; DataInicial, DataFinal: TDateTime): Byte;
    function ValidaDataHoraString(DataHora: string): Boolean;
    procedure InterpolacaoSplineFull(ITempo, DeltaT: MinInteiroSs; ArqDados, ArqSaida: string);  
  end;

implementation

procedure TClasseRotinasGenericas.GeraArqAlturasDll(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs; DataIni, DataFim, Arquivo: string);
{Procedimento que gera o arquivo de Alturas Hor�rias padr�o para a DLL das rotinas Alpha.
OBS: O c�digo da Esta��o Maregr�fica deve estar previamente cadastrado e as datas passadas
por par�metros (DataIni, DataFim) devem estar no formato 'dd/mm/yyyy'. O arquivo ser�
gerado no caminho especificado na vari�vel 'Arquivo'. Se o arquivo j� existe ele ser�
substituido.}
var
  AltHor, EstMaregr: TADOQuery;
  ArqSaida: TextFile;
  NumEstacao, NomeEstacao, Lat, Long, Fuso: string;
  I: Integer;
  sqlQuery : string;
begin
  EstMaregr:= TADOQuery.Create(nil);
  AltHor:= TADOQuery.Create(nil);
  EstMaregr.Connection:= Conexao;
  AltHor.Connection:= Conexao;
  EstMaregr.SQL.Clear;
  EstMaregr.SQL.Add('select * from estacao_maregrafica where cod_estacao_maregrafica = ' + IntToStr(CodEstacao));
  EstMaregr.Open;
  NumEstacao:= FormatFloat('00000', EstMaregr.FieldByName('num_estacao_maregrafica').AsFloat);
  NomeEstacao:= BNDO.formata_string(EstMaregr.FieldByName('nome_estacao_maregrafica').AsString, 38);
  Lat:= BNDO.formata_latlon_mare(EstMaregr.FieldByName('latitude').AsInteger, 'LT');
  Long:= BNDO.formata_latlon_mare(EstMaregr.FieldByName('longitude').AsInteger, 'LG');
  Fuso:= EstMaregr.FieldByName('fuso').AsString;
  EstMaregr.Close;
  FreeAndNil(EstMaregr);
  AssignFile(ArqSaida, Arquivo);
  Rewrite(ArqSaida);
  Writeln(ArqSaida, NumEstacao, BNDO.converte_data_arq(DataIni), BNDO.converte_data_arq(DataFim), NomeEstacao, Lat, Long, Fuso);

  sqlQuery := 'select * from vw_alturas_horarias where cod_estacao_maregrafica = ' + IntToStr(CodEstacao) +
    ' and cod_equipamento_estacao_maregrafica = ' + IntToStr(CodEquipEstacao) + ' and dbo.ConverteData(data_hora) >= ''' + BNDO.ConverteDataAnsi(DataIni) +
    ''' and dbo.ConverteData(data_hora) <= ''' + BNDO.ConverteDataAnsi(DataFim) + ''' and datepart(mi, data_hora) = 00 and ' +
    'datepart(ss, data_hora) = 00 order by data_hora';

  writeln( sqlQuery );

  AltHor.SQL.Add( sqlQuery );
  AltHor.Open;
  AltHor.First;
  while not (AltHor.Eof) do
    begin
      for I:= 1 to 24 do
        begin
          if (AltHor.FieldByName('altura').AsInteger >= 0) then
            Write(ArqSaida, FormatFloat('0000', StrToFloat(AltHor.FieldByName('altura').AsString)))
          else
            Write(ArqSaida, FormatFloat('000', StrToFloat(AltHor.FieldByName('altura').AsString)));
          AltHor.Next;
        end;
      Writeln(ArqSaida);
    end;
  CloseFile(ArqSaida);
  AltHor.Close;
  FreeAndNil(AltHor);
end;

procedure TClasseRotinasGenericas.GeraArqAlturasTempDll(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs; DataIni, DataFim, Arquivo: string);
{Procedimento que gera o arquivo de Alturas Hor�rias padr�o para a DLL das rotinas Alpha.
OBS: O c�digo da Esta��o Maregr�fica deve estar previamente cadastrado e as datas passadas
por par�metros (DataIni, DataFim) devem estar no formato 'dd/mm/yyyy'. O arquivo ser�
gerado no caminho especificado na vari�vel 'Arquivo'. Se o arquivo j� existe ele ser�
substituido.}
var
  AltHor, EstMaregr: TADOQuery;
  ArqSaida: TextFile;
  NumEstacao, NomeEstacao, Lat, Long, Fuso: string;
  I: integer;
begin
  EstMaregr:= TADOQuery.Create(nil);
  AltHor:= TADOQuery.Create(nil);
  EstMaregr.Connection:= Conexao;
  AltHor.Connection:= Conexao;
  EstMaregr.SQL.Clear;
  EstMaregr.SQL.Add('select * from estacao_maregrafica where cod_estacao_maregrafica = ' + IntToStr(CodEstacao));
  EstMaregr.Open;
  NumEstacao:= FormatFloat('00000', EstMaregr.FieldByName('num_estacao_maregrafica').AsFloat);
  NomeEstacao:= BNDO.formata_string(EstMaregr.FieldByName('nome_estacao_maregrafica').AsString, 38);
  Lat:= BNDO.formata_latlon_mare(EstMaregr.FieldByName('latitude').AsInteger, 'LT');
  Long:= BNDO.formata_latlon_mare(EstMaregr.FieldByName('longitude').AsInteger, 'LG');
  Fuso:= EstMaregr.FieldByName('fuso').AsString;
  EstMaregr.Close;
  FreeAndNil(EstMaregr);
  AssignFile(ArqSaida, Arquivo);
  Rewrite(ArqSaida);
  Writeln(ArqSaida, NumEstacao, BNDO.converte_data_arq(DataIni), BNDO.converte_data_arq(DataFim), NomeEstacao, Lat, Long, Fuso);
  AltHor.SQL.Add('select * from vw_alturas_horarias_temp where cod_estacao_maregrafica = ' + IntToStr(CodEstacao) +
    ' and cod_equipamento_estacao_maregrafica = ' + IntToStr(CodEquipEstacao) + ' and dbo.ConverteData(data_hora) >= ''' + BNDO.ConverteDataAnsi(DataIni) +
    ''' and dbo.ConverteData(data_hora) <= ''' + BNDO.ConverteDataAnsi(DataFim) + ''' and datepart(mi, data_hora) = 00 and ' +
    'datepart(ss, data_hora) = 00 order by data_hora');
  AltHor.Open;
  AltHor.First;
  while not (AltHor.Eof) do
    begin
      for I:= 1 to 24 do
        begin
          if (AltHor.FieldByName('altura').AsInteger >= 0) then
            Write(ArqSaida, FormatFloat('0000', StrToFloat(AltHor.FieldByName('altura').AsString)))
          else
            Write(ArqSaida, FormatFloat('000', StrToFloat(AltHor.FieldByName('altura').AsString)));
          AltHor.Next;
        end;
      Writeln(ArqSaida);
    end;
  CloseFile(ArqSaida);
  AltHor.Close;
  FreeAndNil(AltHor);
end;

function TClasseRotinasGenericas.RetornaNumeroEstacao(Conexao: TADOConnection; CodEstacao: PeqInteiroSs): LongoInteiroSs;
var
  EstMaregr: TADOQuery;
begin
  EstMaregr:= TADOQuery.Create(nil);
  EstMaregr.Connection:= Conexao;
  EstMaregr.SQL.Clear;
  EstMaregr.SQL.Add('select * from estacao_maregrafica where cod_estacao_maregrafica = ' + IntToStr(CodEstacao));
  EstMaregr.Open;
  if (EstMaregr.IsEmpty) then
    RetornaNumeroEstacao:= 0
  else
    RetornaNumeroEstacao:= EstMaregr.FieldByName('num_estacao_maregrafica').AsInteger;
  EstMaregr.Close;
  FreeAndNil(EstMaregr);
end;

function TClasseRotinasGenericas.ExistePeriodoConstantesTemp(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs;
  DataIni, DataFim: string): Boolean;
var
  AnaliseMare: TADOQuery;
begin
  AnaliseMare:= TADOQuery.Create(nil);
  AnaliseMare.Connection:= Conexao;
  AnaliseMare.SQL.Clear;
  AnaliseMare.SQL.Add('select * from vw_analise_mares_temp where cod_estacao_maregrafica = ' + IntToStr(CodEstacao) +
    ' and cod_equipamento_estacao_maregrafica = ' + IntToStr(CodEquipEstacao) + ' and data_hora_inicio = ''' + DataIni +
    ' 00:00:00'' and data_hora_fim = ''' + DataFim + ' 00:00:00''');
  AnaliseMare.Open;
  if (AnaliseMare.IsEmpty) then
    ExistePeriodoConstantesTemp:= false
  else
    ExistePeriodoConstantesTemp:= true;
  AnaliseMare.Close;
  FreeAndNil(AnaliseMare);
end;

function TClasseRotinasGenericas.ValidaPeriodoAlturasTempDll(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs;
  DataIni, DataFim: string): Boolean;
{Fun��o para validar um per�odo de Alturas Hor�rias. Se o per�odo informado estiver
cadastrado dia a dia sem quebras (dias pertencentes ao per�odo que n�o est�o cadastrados)
no Banco de Dados a fun��o retorna true. As datas devem estar no formato dd/mm/yyyy.}
var
  AltHor: TADOQuery;
  QtdDias, QtdDiasCadastrados: integer;
begin
  AltHor:= TADOQuery.Create(nil);
  AltHor.Connection:= Conexao;
  AltHor.SQL.Clear;
  AltHor.SQL.Add('select datediff(d, ''' + DataIni + ''', ''' + DataFim + ''') as QtdDias');
  AltHor.Open;
  QtdDias:= AltHor.FieldByName('QtdDias').AsInteger + 1;
  AltHor.Close;
  AltHor.SQL.Clear;
  AltHor.SQL.Add('select count(tmp.DiaObservado) as QtdDiasCadastrados from ' +
    '(select dbo.ConverteData(data_hora) as DiaObservado, count(*) as QtdHorasCheias ' +
    'from vw_alturas_horarias_temp where cod_estacao_maregrafica = ' + IntToStr(CodEstacao) +
    ' and cod_equipamento_estacao_maregrafica = ' + IntToStr(CodEquipEstacao) +
    ' and dbo.ConverteData(data_hora) >= ''' + BNDO.ConverteDataAnsi(DataIni) +
    ''' and dbo.ConverteData(data_hora) <= ''' + BNDO.ConverteDataAnsi(DataFim) +
    ''' and datepart(mi ,data_hora) = 00 and datepart(ss ,data_hora) = 00 ' +
    'group by dbo.ConverteData(data_hora) having count(*) = 24) as tmp');
  AltHor.Open;
  QtdDiasCadastrados:= AltHor.FieldByName('QtdDiasCadastrados').AsInteger;
  AltHor.Close;
  FreeAndNil(AltHor);
  if (QtdDias = QtdDiasCadastrados) then
    ValidaPeriodoAlturasTempDll:= true
  else
    ValidaPeriodoAlturasTempDll:= false;
end;

procedure TClasseRotinasGenericas.GeraArquivoAlturasEquipamento(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs; DataIni,
  DataFim, Arquivo: string; Filtro: MinInteiroSs);
{Procedimento que gera um arquivo texto do Per�odo de Alturas Hor�rias informado.
As vari�veis DataIni e DataFim devem estar no formato 'dd/mm/yyyy'.}
var
  EstMaregr, AltHor: TADOQuery;
  ArqSaida: TextFile;
begin
  EstMaregr:= TADOQuery.Create(nil);
  AltHor:= TADOQuery.Create(nil);
  AltHor.Connection:= Conexao;
  EstMaregr.Connection:= Conexao;
  EstMaregr.SQL.Clear;
  EstMaregr.SQL.Add('select * from estacao_maregrafica where cod_estacao_maregrafica = ' + IntToStr(CodEstacao));
  EstMaregr.Open;
  AssignFile(ArqSaida, Arquivo);
  Rewrite(ArqSaida);
  Writeln(ArqSaida, 'PER�ODO DE ALTURAS OBSERVADAS');
  Writeln(ArqSaida, 'ESTA��O:' + FormatFloat('00000', EstMaregr.FieldByName('num_estacao_maregrafica').AsInteger));
  Writeln(ArqSaida, 'NOME ESTA��O:' + EstMaregr.FieldByName('nome_estacao_maregrafica').AsString);
  Writeln(ArqSaida, 'LATITUDE:' + BNDO.FormataLatLonMareTela(EstMaregr.FieldByName('latitude').AsInteger, 'LT'));
  Writeln(ArqSaida, 'LONGITUDE:' + BNDO.FormataLatLonMareTela(EstMaregr.FieldByName('longitude').AsInteger, 'LG'));
  Writeln(ArqSaida, 'DATA INICIAL:' + DataIni);
  Writeln(ArqSaida, 'DATA FINAL:' + DataFim);
  Writeln(ArqSaida, 'EQUIPAMENTO:' + IntToStr(CodEquipEstacao) + ' - ' + RetornaNomeEquipamento(Conexao, CodEquipEstacao));
  Writeln(ArqSaida, 'ALTURAS EM CENT�METROS');
  Writeln(ArqSaida, 'HOR�RIO PAPA');
  Writeln(ArqSaida, '#');
  EstMaregr.Close;
  AltHor.SQL.Clear;
  AltHor.SQL.Add('select * from vw_alturas_horarias where cod_estacao_maregrafica = ' + IntToStr(CodEstacao) +
    ' and cod_equipamento_estacao_maregrafica = ' + IntToStr(CodEquipEstacao) +
    ' and dbo.ConverteData(data_hora) >= ''' + BNDO.ConverteDataAnsi(DataIni) +
    ''' and dbo.ConverteData(data_hora) <= ''' + BNDO.ConverteDataAnsi(DataFim) + '''');
  if (Filtro = 1) then
    AltHor.SQL.Add(' and datepart(mi, data_hora) = 00 and datepart(ss, data_hora) = 00');
  AltHor.SQL.Add(' order by data_hora');
  AltHor.Open;
  AltHor.First;
  while not (AltHor.Eof) do
    begin
      Writeln(ArqSaida, FormatDateTime('dd/mm/yyyy hh:mm', AltHor.FieldByName('data_hora').AsDateTime) +
        ';' + AltHor.FieldByName('altura').AsString + ';');
      AltHor.Next;
    end;
  CloseFile(ArqSaida);
  AltHor.Close;
  FreeAndNil(EstMaregr);
  FreeAndNil(AltHor);
end;

function TClasseRotinasGenericas.ValidaPeriodoAlturasDll(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs;
  DataIni, DataFim: string): Boolean;
{Fun��o para validar um per�odo de Alturas Hor�rias. Se o per�odo informado estiver
cadastrado dia a dia sem quebras (dias pertencentes ao per�odo que n�o est�o cadastrados)
no Banco de Dados a fun��o retorna true. As datas devem estar no formato dd/mm/yyyy.}
var
  AltHor: TADOQuery;
  QtdDias, QtdDiasCadastrados: integer;
begin
  AltHor:= TADOQuery.Create(nil);
  AltHor.Connection:= Conexao;
  QtdDias:= RetornaDiferencaPeriodo(StrToDate(DataIni), StrToDate(DataFim), 3) + 1;
  AltHor.SQL.Clear;
  AltHor.SQL.Add('select count(tmp.DiaObservado) as QtdDiasCadastrados from ' +
    '(select dbo.ConverteData(data_hora) as DiaObservado, count(*) as QtdHorasCheias ' +
    'from vw_alturas_horarias where cod_estacao_maregrafica = ' + IntToStr(CodEstacao) +
    ' and cod_equipamento_estacao_maregrafica = ' + IntToStr(CodEquipEstacao) +
    ' and dbo.ConverteData(data_hora) >= ''' + BNDO.ConverteDataAnsi(DataIni) +
    ''' and dbo.ConverteData(data_hora) <= ''' + BNDO.ConverteDataAnsi(DataFim) +
    ''' and datepart(mi ,data_hora) = 00 and datepart(ss ,data_hora) = 00 ' +
    'group by dbo.ConverteData(data_hora) having count(*) = 24) as tmp');
  AltHor.Open;
  QtdDiasCadastrados:= AltHor.FieldByName('QtdDiasCadastrados').AsInteger;
  AltHor.Close;
  FreeAndNil(AltHor);
  if (QtdDias = QtdDiasCadastrados) then
    ValidaPeriodoAlturasDll:= true
  else
    ValidaPeriodoAlturasDll:= false;
end;

function TClasseRotinasGenericas.ValidaPeriodoAlturasEquipamento(Conexao: TADOConnection; Area: MinInteiroSs; CodEquipEstacao: PeqInteiroSs;
  DataIni, DataFim: TDate): MinInteiroSs;
{Fun��o para validar um per�odo de Alturas Hor�rias. Se o per�odo informado estiver
cadastrado dia a dia sem quebras (dias pertencentes ao per�odo que n�o est�o cadastrados)
no Banco de Dados a fun��o retorna true. As datas devem estar no formato dd/mm/yyyy.}
var
  AltHor: TADOQuery;
  TaxaAmostragem: MinInteiroSs;
  QtdDias, QtdAlturas: LongoInteiroSs;
begin
  AltHor:= TADOQuery.Create(nil);
  AltHor.Connection:= Conexao;
  QtdDias:= RetornaDiferencaPeriodo(DataIni, DataFim, 3) + 1;
  TaxaAmostragem:= RetornaTaxaAmostragemEquipamento(Conexao, CodEquipEstacao);
  AltHor.SQL.Clear;
  AltHor.SQL.Add('select count(*) as QtdAlturas from ');
  case (Area) of
    0: AltHor.SQL.Add('mare.dbo.vw_alturas_unificadas ');
    1: AltHor.SQL.Add('mare.dbo.vw_alturas_horarias ');
    2: AltHor.SQL.Add('mare.dbo.vw_alturas_horarias_temp ');
  end;
  AltHor.SQL.Add('where cod_equipamento_estacao_maregrafica = ' + IntToStr(CodEquipEstacao) + ' ');
	AltHor.SQL.Add('and mare.dbo.ConverteData(data_hora) >= ''' + BNDO.ConverteDataAnsi(FormatDateTime('dd/mm/yyyy', DataIni)) + ''' ');
	AltHor.SQL.Add('and mare.dbo.ConverteData(data_hora) <= ''' + BNDO.ConverteDataAnsi(FormatDateTime('dd/mm/yyyy', DataFim)) + ''' ');
	AltHor.SQL.Add('and (datepart(mi ,data_hora) % ' + IntToStr(TaxaAmostragem) + ') = 0 ');
	AltHor.SQL.Add('and datepart(ss ,data_hora) = 0 ');
	AltHor.SQL.Add('and datepart(ms, data_hora) = 0 ');
	AltHor.SQL.Add('and altura is not null');
  AltHor.Open;
  QtdAlturas:= AltHor.FieldByName('QtdAlturas').AsInteger;
  AltHor.Close;
  FreeAndNil(AltHor);
  if (QtdAlturas = 0) then
    ValidaPeriodoAlturasEquipamento:= 1
  else
    begin
      if ((QtdDias * (1440 / TaxaAmostragem)) = QtdAlturas) then
        ValidaPeriodoAlturasEquipamento:= 0
      else
        ValidaPeriodoAlturasEquipamento:= 2;
    end;
end;

procedure TClasseRotinasGenericas.GeraArqConstantesDll(Conexao: TADOConnection; CodEstacao: PeqInteiroSs; CodAnalise: integer;
  Arquivo: string);
var
  EstMaregr, AnaliseMares, Componente: TADOQuery;
  Arq: TextFile;
begin
  EstMaregr:= TADOQuery.Create(nil);
  AnaliseMares:= TADOQuery.Create(nil);
  Componente:= TADOQuery.Create(nil);
  EstMaregr.Connection:= Conexao;
  AnaliseMares.Connection:= Conexao;
  Componente.Connection:= Conexao;
  AssignFile(Arq, Arquivo);
  Rewrite(Arq);
  EstMaregr.SQL.Clear;
  EstMaregr.SQL.Add('select * from estacao_maregrafica where cod_estacao_maregrafica = ' + IntToStr(CodEstacao));
  AnaliseMares.SQL.Clear;
  AnaliseMares.SQL.Add('select * from vw_analise_mares where cod_analise_mares = ' + IntToStr(CodAnalise));
  EstMaregr.Open;
  AnaliseMares.Open;
  Write(Arq, FormatFloat('000', AnaliseMares.FieldByName('num_componentes').AsFloat));
  Write(Arq, FormatFloat('0000000.00', AnaliseMares.FieldByName('z0').AsFloat));
  Write(Arq, FormatDateTime('ddmmyyyy', AnaliseMares.FieldByName('data_hora_inicio').AsDateTime));
  Write(Arq, FormatDateTime('ddmmyyyy', AnaliseMares.FieldByName('data_hora_fim').AsDateTime));
  Write(Arq, FormatFloat('00000', EstMaregr.FieldByName('num_estacao_maregrafica').AsFloat));
  Write(Arq, BNDO.formata_string(EstMaregr.FieldByName('nome_estacao_maregrafica').AsString, 38));
  Write(Arq, BNDO.formata_latlon_mare(EstMaregr.FieldByName('latitude').AsInteger, 'LT'));
  Write(Arq, BNDO.formata_latlon_mare(EstMaregr.FieldByName('longitude').AsInteger, 'LG'));
  Write(Arq, EstMaregr.FieldByName('fuso').AsString);
  EstMaregr.Close;
  AnaliseMares.Close;
  Writeln(Arq);
  Componente.SQL.Clear;
  Componente.SQL.Add('select ct.cod_analise_mares, ct.g, ct.h, cp.* from constantes_analise ct, ' +
    '(select * from componente) as cp where cod_analise_mares = ' + IntToStr(CodAnalise) +
    ' and ct.cod_componente = cp.cod_componente order by velocidade');
  Componente.Open;
  Componente.First;
  while not(Componente.Eof) do
    begin
      Write(Arq, Componente.FieldByName('tipo').AsString);
      Write(Arq, BNDO.formata_string(Componente.FieldByName('nome').AsString, 7));
      Write(Arq, FormatFloat('000.0000000', Componente.FieldByName('velocidade').AsFloat));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_1').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_2').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_3').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_4').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_5').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_6').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_7').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_8').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_9').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_10').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_11').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_12').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_13').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_14').AsString));
      Write(Arq, Formatfloat('00000.00', Componente.FieldByName('h').AsFloat));
      Write(Arq, Formatfloat('00000.00', Componente.FieldByName('g').AsFloat));
      Writeln(Arq);
      Componente.Next;
    end;
  Componente.Close;
  CloseFile(Arq);
  FreeAndNil(EstMaregr);
  FreeAndNil(AnaliseMares);
  FreeAndNil(Componente);
end;

function TClasseRotinasGenericas.FormataIndiceComponente(Indice: string): string;
var
  IndiceAux: string;
begin
  if (Length(Indice) = 0) then
    IndiceAux:= '  '
  else
    if (Length(Indice) = 1) then
      IndiceAux:= ' ' + Indice
    else
      IndiceAux:= Indice;
  FormataIndiceComponente:= IndiceAux;
end;

procedure TClasseRotinasGenericas.ConcatenaArquivos(ArqPrin, ArqDados: string; Ignorar: PeqInteiroSs; DelArqDados: Boolean);
var
  ArquivoPrin, ArquivoDados: TextFile;
  Linha: string;
  I: integer;
begin
  AssignFile(ArquivoPrin, Trim(ArqPrin));
  AssignFile(ArquivoDados, Trim(ArqDados));
  if (FileExists(ArqPrin)) then
    Append(ArquivoPrin)
  else
    Rewrite(ArquivoPrin);
  Reset(ArquivoDados);  
  I:= 0;
  while ((I <> Ignorar) and (not Eof(ArquivoDados))) do
    begin
      Readln(ArquivoDados, Linha);
      I:= I + 1;
    end;
  while not (Eof(ArquivoDados)) do
    begin
      Readln(ArquivoDados, Linha);
      Writeln(ArquivoPrin, Linha);
    end;
  CloseFile(ArquivoDados);
  CloseFile(ArquivoPrin);
  if (DelArqDados) then
    DeleteFile(ArqDados);
end;

function TClasseRotinasGenericas.ExisteReducao(Conexao: TADOConnection; CodEstacao: PeqInteiroSs; CodAnalise: LongoInteiroSs): Boolean;
var
  ReducSond: TADOQuery;
begin
  ReducSond:= TADOQuery.Create(nil);
  ReducSond.Connection:= Conexao;
  ReducSond.SQL.Clear;
  ReducSond.SQL.Add('select * from reducao_sondagem where cod_estacao_maregrafica = ' + IntToStr(CodEstacao) +
    ' and cod_analise_mares = ' + IntToStr(CodAnalise));
  ReducSond.Open;
  if (ReducSond.IsEmpty) then
    ExisteReducao:= false
  else
    ExisteReducao:= true;
  ReducSond.Close;
  FreeAndNil(ReducSond);
end;

function TClasseRotinasGenericas.ExistePeriodoConstantes(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs;
  DataIni, DataFim: string): Boolean;
var
  AnaliseMare: TADOQuery;
begin
  AnaliseMare:= TADOQuery.Create(nil);
  AnaliseMare.Connection:= Conexao;
  AnaliseMare.SQL.Clear;
  AnaliseMare.SQL.Add('select * from vw_analise_mares where cod_estacao_maregrafica = ' + IntToStr(CodEstacao) +
    ' and cod_equipamento_estacao_maregrafica = ' + IntToStr(CodEquipEstacao) + ' and data_hora_inicio = ''' + DataIni +
    ' 00:00:00'' and data_hora_fim = ''' + DataFim + ' 00:00:00''');
  AnaliseMare.Open;
  if (AnaliseMare.IsEmpty) then
    ExistePeriodoConstantes:= false
  else
    ExistePeriodoConstantes:= true;
  AnaliseMare.Close;
  FreeAndNil(AnaliseMare);
end;

function TClasseRotinasGenericas.RetornaCodigoEstacao(Conexao: TADOConnection; NumEstacao: LongoInteiroSs): PeqInteiroSs;
var
  EstMaregr: TADOQuery;
begin
  EstMaregr:= TADOQuery.Create(nil);
  EstMaregr.Connection:= Conexao;
  EstMaregr.SQL.Clear;
  EstMaregr.SQL.Add('select cod_estacao_maregrafica from estacao_maregrafica where num_estacao_maregrafica = ' + IntToStr(NumEstacao));
  EstMaregr.Open;
  if (EstMaregr.IsEmpty) then
    RetornaCodigoEstacao:= 0
  else
    RetornaCodigoEstacao:= EstMaregr.FieldByName('cod_estacao_maregrafica').AsInteger;
  EstMaregr.Close;
  FreeAndNil(EstMaregr);
end;

function TClasseRotinasGenericas.RetornaNomeEquipamento(Conexao: TADOConnection; CodEquipEstacao: PeqInteiroSs): string;
var
  Query: TADOQuery;
begin
  Query:= TADOQuery.Create(nil);
  Query.Connection:= Conexao;
  Query.SQL.Clear;
  Query.SQL.Add('select e.nome_equipamento from equipamento e, equipamento_estacao_maregrafica em ' +
    'where em.cod_equipamento = e.cod_equipamento and em.cod_equipamento_estacao_maregrafica = ' +
    IntToStr(CodEquipEstacao));
  Query.Open;
  if (Query.IsEmpty) then
    RetornaNomeEquipamento:= ''
  else
    RetornaNomeEquipamento:= Query.FieldByName('nome_equipamento').AsString;
  Query.Close;
  FreeAndNil(Query);
end;

function TClasseRotinasGenericas.RetornaNomeEstacao(Conexao: TADOConnection; CodEstacao: PeqInteiroSs): string;
var
  Query: TADOQuery;
begin
  Query:= TADOQuery.Create(nil);
  Query.Connection:= Conexao;
  Query.SQL.Clear;
  Query.SQL.Add('select nome_estacao_maregrafica from estacao_maregrafica where cod_estacao_maregrafica = ' +
    IntToStr(CodEstacao));
  Query.Open;
  if (Query.IsEmpty) then
    RetornaNomeEstacao:= ''
  else
    RetornaNomeEstacao:= Query.FieldByName('nome_estacao_maregrafica').AsString;
  Query.Close;
  FreeAndNil(Query);
end;

procedure TClasseRotinasGenericas.GeraArqConstantesPadraoDll(Conexao: TADOConnection; CodEstacao: PeqInteiroSs; Arquivo: string);
var
  EstMaregr, AnaliseMares, Componente: TADOQuery;
  CodAnalise: integer;
  Arq: TextFile;
begin
  EstMaregr:= TADOQuery.Create(nil);
  AnaliseMares:= TADOQuery.Create(nil);
  Componente:= TADOQuery.Create(nil);
  EstMaregr.Connection:= Conexao;
  AnaliseMares.Connection:= Conexao;
  Componente.Connection:= Conexao;
  AssignFile(Arq, Arquivo);
  Rewrite(Arq);
  EstMaregr.SQL.Clear;
  EstMaregr.SQL.Add('select * from estacao_maregrafica where cod_estacao_maregrafica = ' + IntToStr(CodEstacao));
  AnaliseMares.SQL.Clear;
  AnaliseMares.SQL.Add('select * from vw_analise_mares where cod_estacao_maregrafica = ' + IntToStr(CodEstacao) +
    ' and periodo_padrao = ''S''');
  EstMaregr.Open;
  AnaliseMares.Open;
  CodAnalise:= AnaliseMares.FieldByName('cod_analise_mares').AsInteger;
  Write(Arq, FormatFloat('000', AnaliseMares.FieldByName('num_componentes').AsFloat));
  Write(Arq, FormatFloat('0000000.00', AnaliseMares.FieldByName('z0').AsFloat));
  Write(Arq, FormatDateTime('ddmmyyyy', AnaliseMares.FieldByName('data_hora_inicio').AsDateTime));
  Write(Arq, FormatDateTime('ddmmyyyy', AnaliseMares.FieldByName('data_hora_fim').AsDateTime));
  Write(Arq, FormatFloat('00000', EstMaregr.FieldByName('num_estacao_maregrafica').AsFloat));
  Write(Arq, BNDO.formata_string(EstMaregr.FieldByName('nome_estacao_maregrafica').AsString, 38));
  Write(Arq, BNDO.formata_latlon_mare(EstMaregr.FieldByName('latitude').AsInteger, 'LT'));
  Write(Arq, BNDO.formata_latlon_mare(EstMaregr.FieldByName('longitude').AsInteger, 'LG'));
  Write(Arq, EstMaregr.FieldByName('fuso').AsString);
  EstMaregr.Close;
  AnaliseMares.Close;
  Writeln(Arq);
  Componente.SQL.Clear;
  Componente.SQL.Add('select ct.cod_analise_mares, ct.g, ct.h, cp.* from constantes_analise ct, ' +
    '(select * from componente) as cp where cod_analise_mares = ' + IntToStr(CodAnalise) +
    ' and ct.cod_componente = cp.cod_componente order by velocidade');
  Componente.Open;
  Componente.First;
  while not(Componente.Eof) do
    begin
      Write(Arq, Componente.FieldByName('tipo').AsString);
      Write(Arq, BNDO.formata_string(Componente.FieldByName('nome').AsString, 7));
      Write(Arq, FormatFloat('000.0000000', Componente.FieldByName('velocidade').AsFloat));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_1').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_2').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_3').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_4').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_5').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_6').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_7').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_8').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_9').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_10').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_11').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_12').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_13').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_14').AsString));
      Write(Arq, Formatfloat('00000.00', Componente.FieldByName('h').AsFloat));
      Write(Arq, Formatfloat('00000.00', Componente.FieldByName('g').AsFloat));
      Writeln(Arq);
      Componente.Next;
    end;
  Componente.Close;
  CloseFile(Arq);
  FreeAndNil(EstMaregr);
  FreeAndNil(AnaliseMares);
  FreeAndNil(Componente);
end;

function TClasseRotinasGenericas.RetornaCodigoAnalisePeriodoPadrao(Conexao: TADOConnection; CodEstacao: PeqInteiroSs): Integer;
var
  Query: TADOQuery;
begin
  Query:= TADOQuery.Create(nil);
  Query.Connection:= Conexao;
  Query.SQL.Clear;
  Query.SQL.Add('select cod_analise_mares from mare.dbo.vw_analise_mares where cod_estacao_maregrafica = ' +
    IntToStr(CodEstacao) + ' and periodo_padrao = ''S''');
  Query.Open;
  if (Query.IsEmpty) then
    RetornaCodigoAnalisePeriodoPadrao:= 0
  else
    RetornaCodigoAnalisePeriodoPadrao:= Query.FieldByName('cod_analise_mares').AsInteger;
  Query.Close;
  FreeAndNil(Query);
end;

function TClasseRotinasGenericas.RetornaZ0Analise(Conexao: TADOConnection; CodAnalise: Integer): Real;
var
  Query: TADOQuery;
begin
  Query:= TADOQuery.Create(nil);
  Query.Connection:= Conexao;
  Query.SQL.Clear;
  Query.SQL.Add('select z0 from vw_analise_mares where cod_analise_mares = ' + IntToStr(CodAnalise));
  Query.Open;
  if (Query.IsEmpty) then
    RetornaZ0Analise:= 0
  else
    RetornaZ0Analise:= Query.FieldByName('z0').AsFloat;
  Query.Close;
  FreeAndNil(Query);
end;

function TClasseRotinasGenericas.RetornaQtdAlturasObsHorasCheias(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs;
  DataIni, DataFim: string): Integer;
var
  AltHor: TADOQuery;
begin
  AltHor:= TADOQuery.Create(nil);
  AltHor.Connection:= Conexao;
  AltHor.SQL.Clear;
  AltHor.SQL.Add('select count(*) as QtdAlturas from vw_alturas_horarias ' +
    'where cod_estacao_maregrafica = ' + IntToStr(CodEstacao) +
    ' and cod_equipamento_estacao_maregrafica = ' + IntToStr(CodEquipEstacao) +
    ' and dbo.ConverteData(data_hora) >= ''' + BNDO.ConverteDataAnsi(DataIni) +
    ''' and dbo.ConverteData(data_hora) <= ''' + BNDO.ConverteDataAnsi(DataFim) +
    ''' and datepart(mi, data_hora) = 00 and datepart(ss, data_hora) = 00');
  AltHor.Open;
  RetornaQtdAlturasObsHorasCheias:= AltHor.FieldByName('QtdAlturas').AsInteger;
  AltHor.Close;
  FreeAndNil(AltHor);
end;

function TClasseRotinasGenericas.ExisteReducaoTemp(Conexao: TADOConnection; CodEstacao: PeqInteiroSs; CodAnalise: LongoInteiroSs): Boolean;
var
  ReducSond: TADOQuery;
begin
  ReducSond:= TADOQuery.Create(nil);
  ReducSond.Connection:= Conexao;
  ReducSond.SQL.Clear;
  ReducSond.SQL.Add('select * from reducao_sondagem_temp where cod_estacao_maregrafica = ' + IntToStr(CodEstacao) +
    ' and cod_analise_mares = ' + IntToStr(CodAnalise));
  ReducSond.Open;
  if (ReducSond.IsEmpty) then
    ExisteReducaoTemp:= False
  else
    ExisteReducaoTemp:= True;
  ReducSond.Close;
  FreeAndNil(ReducSond);
end;

function TClasseRotinasGenericas.ExisteAlturasPeriodoTemp(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs; DataIni, DataFim: string): Boolean;
{Fun��o para verificar se existem Alturas Hor�rias cadastradas dentro do per�odo informado.
Caso existam a fun��o retorna true.}
var
  AltHor: TADOQuery;
begin
  AltHor:= TADOQuery.Create(nil);
  AltHor.Connection:= Conexao;
  AltHor.SQL.Clear;
  AltHor.SQL.Add('select count(*) as contador from vw_alturas_horarias_temp');
  AltHor.SQL.Add(' where cod_estacao_maregrafica = ' + IntToStr(CodEstacao));
  AltHor.SQL.Add(' and cod_equipamento_estacao_maregrafica = ' + IntToStr(CodEquipEstacao));
  AltHor.SQL.Add(' and dbo.ConverteData(data_hora) >= ''' + BNDO.ConverteDataAnsi(DataIni) + '''');
  AltHor.SQL.Add(' and dbo.ConverteData(data_hora) <= ''' + BNDO.ConverteDataAnsi(DataFim) + '''');
  AltHor.Open;
  if (AltHor.FieldByName('contador').AsInteger = 0) then
    ExisteAlturasPeriodoTemp:= False
  else
    ExisteAlturasPeriodoTemp:= True;
  AltHor.Close;
  FreeAndNil(AltHor);
end;

procedure TClasseRotinasGenericas.GeraArquivoAlturasTempEquipamento(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs; DataInicial,
  DataFinal: TDate; Arquivo: string; Filtro: Boolean);
{Procedimento que gera um arquivo texto do Per�odo de Alturas Hor�rias informado.
As vari�veis DataInicial e DataFinal devem estar no formato 'dd/mm/yyyy'.}
var
  EstMaregr, AltHor: TADOQuery;
  ArqSaida: TextFile;
  TaxaAmostragem: MinInteiroSs;
  QtdDias, QtdAlturas, I: Integer;
  DataAux: TDateTime;
begin
  if not (Filtro) then
    TaxaAmostragem:= RetornaTaxaAmostragemEquipamento(Conexao, CodEquipEstacao)
  else
    TaxaAmostragem:= 60;
  QtdDias:= RetornaDiferencaPeriodo(DataInicial, DataFinal, 3) + 1;
  QtdAlturas:= (((60 div TaxaAmostragem) * 24) * QtdDias);
  EstMaregr:= TADOQuery.Create(nil);
  AltHor:= TADOQuery.Create(nil);
  AltHor.Connection:= Conexao;
  EstMaregr.Connection:= Conexao;
  EstMaregr.SQL.Clear;
  EstMaregr.SQL.Add('select num_estacao_maregrafica, nome_estacao_maregrafica, latitude, longitude ');
  EstMaregr.SQL.Add('from mare.dbo.estacao_maregrafica where cod_estacao_maregrafica = ' + IntToStr(CodEstacao));
  EstMaregr.Open;
  AssignFile(ArqSaida, Arquivo);
  Rewrite(ArqSaida);
  Writeln(ArqSaida, 'PER�ODO DE ALTURAS OBSERVADAS');
  Writeln(ArqSaida, 'ESTA��O:' + FormatFloat('00000', EstMaregr.FieldByName('num_estacao_maregrafica').AsInteger));
  Writeln(ArqSaida, 'NOME ESTA��O:' + EstMaregr.FieldByName('nome_estacao_maregrafica').AsString);
  Writeln(ArqSaida, 'LATITUDE:' + BNDO.FormataLatLonMareTela(EstMaregr.FieldByName('latitude').AsInteger, 'LT'));
  Writeln(ArqSaida, 'LONGITUDE:' + BNDO.FormataLatLonMareTela(EstMaregr.FieldByName('longitude').AsInteger, 'LG'));
  Writeln(ArqSaida, 'DATA INICIAL:' + FormatDateTime('dd/mm/yyyy', DataInicial));
  Writeln(ArqSaida, 'DATA FINAL:' + FormatDateTime('dd/mm/yyyy', DataFinal));
  Writeln(ArqSaida, 'EQUIPAMENTO:' + IntToStr(CodEquipEstacao) + ' - ' + RetornaNomeEquipamento(Conexao, CodEquipEstacao));
  Writeln(ArqSaida, 'ALTURAS EM CENT�METROS');
  Writeln(ArqSaida, 'HOR�RIO PAPA');
  Writeln(ArqSaida, '#');
  EstMaregr.Close;
  AltHor.SQL.Clear;
  AltHor.SQL.Add('select data_hora, altura from mare.dbo.vw_alturas_horarias_temp where cod_estacao_maregrafica = ' + IntToStr(CodEstacao) +
    ' and cod_equipamento_estacao_maregrafica = ' + IntToStr(CodEquipEstacao) +
    ' and dbo.ConverteData(data_hora) >= ''' + BNDO.ConverteDataAnsi(FormatDateTime('dd/mm/yyyy', DataInicial)) +
    ''' and dbo.ConverteData(data_hora) <= ''' + BNDO.ConverteDataAnsi(FormatDateTime('dd/mm/yyyy', DataFinal)) + '''');
  if (Filtro) then
    AltHor.SQL.Add(' and datepart(mi, data_hora) = 00 and datepart(ss, data_hora) = 00');
  AltHor.SQL.Add(' order by data_hora');
  AltHor.Open;
  AltHor.First;
  I:= 1;
  DataAux:= StrToDateTime(FormatDateTime('dd/mm/yyyy', DataInicial) + ' ' + '00:00');
  while (I <= QtdAlturas) do
    begin
      if ((AltHor.Eof) or (AltHor.FieldByName('data_hora').AsDateTime > DataAux)) then
        begin
          Writeln(ArqSaida, FormatDateTime('dd/mm/yyyy hh:mm', DataAux) + ';;');
          DataAux:= IdentaDataHora(DataAux, TaxaAmostragem);
          I:= I + 1;
        end
      else
        begin
          Writeln(ArqSaida, FormatDateTime('dd/mm/yyyy hh:mm', AltHor.FieldByName('data_hora').AsDateTime) +
            ';' + AltHor.FieldByName('altura').AsString + ';');
          if (AltHor.FieldByName('data_hora').AsDateTime = DataAux) then
            begin
              DataAux:= IdentaDataHora(DataAux, TaxaAmostragem);
              I:= I + 1;
              AltHor.Next;
            end
          else
            AltHor.Next;
        end;
    end;
  CloseFile(ArqSaida);
  AltHor.Close;
  FreeAndNil(EstMaregr);
  FreeAndNil(AltHor);
end;

function TClasseRotinasGenericas.ValidaPeriodoAlturasTempEquipamento(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: integer;
  DataIni, DataFim: string): Boolean;
{Fun��o para validar um per�odo de Alturas Hor�rias. Se o per�odo informado estiver
cadastrado dia a dia sem quebras (dias pertencentes ao per�odo que n�o est�o cadastrados)
no Banco de Dados a fun��o retorna true. As datas devem estar no formato dd/mm/yyyy.}
var
  AltHor: TADOQuery;
  QtdDias, QtdDiasCadastrados: Integer;
begin
  AltHor:= TADOQuery.Create(nil);
  AltHor.Connection:= Conexao;
  QtdDias:= RetornaDiferencaPeriodo(StrToDate(DataIni), StrToDate(DataFim), 3) + 1;
  AltHor.SQL.Clear;
  AltHor.SQL.Add('select count(tmp.DiaObservado) as QtdDiasCadastrados from ' +
    '(select dbo.ConverteData(data_hora) as DiaObservado, count(*) as QtdAlturas ' +
    'from vw_alturas_horarias_temp where cod_estacao_maregrafica = ' + IntToStr(CodEstacao) +
    ' and cod_equipamento_estacao_maregrafica = ' + IntToStr(CodEquipEstacao) +
    ' and dbo.ConverteData(data_hora) >= ''' + BNDO.ConverteDataAnsi(DataIni) +
    ''' and dbo.ConverteData(data_hora) <= ''' + BNDO.ConverteDataAnsi(DataFim) +
    ''' group by dbo.ConverteData(data_hora)) as tmp');
  AltHor.Open;
  QtdDiasCadastrados:= AltHor.FieldByName('QtdDiasCadastrados').AsInteger;
  AltHor.Close;
  FreeAndNil(AltHor);
  if (QtdDias = QtdDiasCadastrados) then
    ValidaPeriodoAlturasTempEquipamento:= True
  else
    ValidaPeriodoAlturasTempEquipamento:= False;
end;

procedure TClasseRotinasGenericas.ExcluiPeriodoAlturasTemp(Conexao: TADOConnection; CodEquipEstacao: PeqInteiroSs;
  DataIni, DataFim: string);
var
  Query: TADOQuery;
begin
  Query:= TADOQuery.Create(nil);
  Query.Connection:= Conexao;
  Query.SQL.Clear;
  Query.SQL.Add('delete from alturas_horarias_temp where cod_equipamento_estacao_maregrafica = ' + IntToStr(CodEquipEstacao));
  Query.SQL.Add(' and dbo.ConverteData(data_hora) >= ''' + BNDO.ConverteDataAnsi(DataIni) + '''');
  Query.SQL.Add(' and dbo.ConverteData(data_hora) <= ''' + BNDO.ConverteDataAnsi(DataFim) + '''');
  Query.ExecSQL;
  Query.Close;
  FreeAndNil(Query);
end;

function TClasseRotinasGenericas.ExisteAlturasPeriodo(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs; DataIni, DataFim: string): Boolean;
{Fun��o para verificar se existem Alturas Hor�rias cadastradas dentro do per�odo informado.
Caso existam a fun��o retorna true.}
var
  AltHor: TADOQuery;
begin
  AltHor:= TADOQuery.Create(nil);
  AltHor.Connection:= Conexao;
  AltHor.SQL.Clear;
  AltHor.SQL.Add('select count(*) as contador from vw_alturas_horarias');
  AltHor.SQL.Add(' where cod_estacao_maregrafica = ' + IntToStr(CodEstacao));
  AltHor.SQL.Add(' and cod_equipamento_estacao_maregrafica = ' + IntToStr(CodEquipEstacao));
  AltHor.SQL.Add(' and dbo.ConverteData(data_hora) >= ''' + BNDO.ConverteDataAnsi(DataIni) + '''');
  AltHor.SQL.Add(' and dbo.ConverteData(data_hora) <= ''' + BNDO.ConverteDataAnsi(DataFim) + '''');
  AltHor.Open;
  if (AltHor.FieldByName('contador').AsInteger = 0) then
    ExisteAlturasPeriodo:= False
  else
    ExisteAlturasPeriodo:= True;
  AltHor.Close;
  FreeAndNil(AltHor);
end;

function TClasseRotinasGenericas.RetornaNumeroEstacaoAnalise(Conexao: TADOConnection; Area: MinInteiroSs; CodAnalise: Integer): LongoInteiroSs;
var
  Query: TADOQuery;
begin
  Query:= TADOQuery.Create(nil);
  Query.Connection:= Conexao;
  Query.SQL.Clear;
  Query.SQL.Add('select em.num_estacao_maregrafica from ');
  case (Area) of
    1: Query.SQL.Add('vw_analise_mares vam ');
    2: Query.SQL.Add('vw_analise_mares_temp vam ');
  end;  
  Query.SQL.Add('inner join estacao_maregrafica em ');
  Query.SQL.Add('on vam.cod_estacao_maregrafica = em.cod_estacao_maregrafica ');
  Query.SQL.Add('where vam.cod_analise_mares = ' + IntToStr(CodAnalise));
  Query.Open;
  if (Query.IsEmpty) then
    RetornaNumeroEstacaoAnalise:= 0
  else
    RetornaNumeroEstacaoAnalise:= Query.FieldByName('num_estacao_maregrafica').AsInteger;
  Query.Close;
  FreeAndNil(Query);
end;

function TClasseRotinasGenericas.RetornaCodigoEstacaoAnalise(Conexao: TADOConnection; Area: MinInteiroSs; CodAnalise: Integer): PeqInteiroSs;
var
  Query: TADOQuery;
begin
  Query:= TADOQuery.Create(nil);
  Query.Connection:= Conexao;
  Query.SQL.Clear;
  Query.SQL.Add('select cod_estacao_maregrafica from ');
  case (Area) of
    1: Query.SQL.Add('vw_analise_mares ');
    2: Query.SQL.Add('vw_analise_mares_temp ');
  end;
  Query.SQL.Add('where cod_analise_mares = ' + IntToStr(CodAnalise));
  Query.Open;
  if (Query.IsEmpty) then
    RetornaCodigoEstacaoAnalise:= 0
  else
    RetornaCodigoEstacaoAnalise:= Query.FieldByName('cod_estacao_maregrafica').AsInteger;
  Query.Close;
  FreeAndNil(Query);
end;

procedure TClasseRotinasGenericas.GeraArquivoConstantesDll(Conexao: TADOConnection; Area: MinInteiroSs; CodAnalise: Integer; Arquivo: string);
var
  EstMaregr, AnaliseMares, Componente: TADOQuery;
  Arq: TextFile;
  CodEstacao: PeqInteiroSs;
begin

  writeln('Gerando constantes em ' + Arquivo );

  EstMaregr:= TADOQuery.Create(nil);
  AnaliseMares:= TADOQuery.Create(nil);
  Componente:= TADOQuery.Create(nil);
  EstMaregr.Connection:= Conexao;
  AnaliseMares.Connection:= Conexao;
  Componente.Connection:= Conexao;
  CodEstacao:= RetornaCodigoEstacaoAnalise(Conexao, Area, CodAnalise);
  AssignFile(Arq, Arquivo);
  Rewrite(Arq);
  EstMaregr.SQL.Clear;
  EstMaregr.SQL.Add('select num_estacao_maregrafica, nome_estacao_maregrafica, latitude, longitude, fuso ');
  EstMaregr.SQL.Add('from estacao_maregrafica where cod_estacao_maregrafica = ' + IntToStr(CodEstacao));
  AnaliseMares.SQL.Clear;
  AnaliseMares.SQL.Add('select data_hora_inicio, data_hora_fim, z0, num_componentes from ');
  case (Area) of
    1: AnaliseMares.SQL.Add('vw_analise_mares ');
    2: AnaliseMares.SQL.Add('vw_analise_mares_temp ');
  else
    AnaliseMares.SQL.Add('vw_analise_mares ');
  end;
  AnaliseMares.SQL.Add('where cod_analise_mares = ' + IntToStr(CodAnalise));
  EstMaregr.Open;
  AnaliseMares.Open;
  Write(Arq, FormatFloat('000', AnaliseMares.FieldByName('num_componentes').AsFloat));
  Write(Arq, FormatFloat('0000000.00', AnaliseMares.FieldByName('z0').AsFloat));
  Write(Arq, FormatDateTime('ddmmyyyy', AnaliseMares.FieldByName('data_hora_inicio').AsDateTime));
  Write(Arq, FormatDateTime('ddmmyyyy', AnaliseMares.FieldByName('data_hora_fim').AsDateTime));
  Write(Arq, FormatFloat('00000', EstMaregr.FieldByName('num_estacao_maregrafica').AsFloat));
  Write(Arq, BNDO.formata_string(EstMaregr.FieldByName('nome_estacao_maregrafica').AsString, 38));
  Write(Arq, BNDO.formata_latlon_mare(EstMaregr.FieldByName('latitude').AsInteger, 'LT'));
  Write(Arq, BNDO.formata_latlon_mare(EstMaregr.FieldByName('longitude').AsInteger, 'LG'));
  Write(Arq, EstMaregr.FieldByName('fuso').AsString);
  EstMaregr.Close;
  AnaliseMares.Close;
  Writeln(Arq);
  Componente.SQL.Clear;
  Componente.SQL.Add('select ct.cod_analise_mares, ct.g, ct.h, cp.* from ');
  case (Area) of
    1: Componente.SQL.Add('constantes_analise ct, ');
    2: Componente.SQL.Add('constantes_analise_temp ct, ');
  else
    Componente.SQL.Add('constantes_analise ct, ');
  end;
  Componente.SQL.Add('(select * from componente) as cp where cod_analise_mares = ' + IntToStr(CodAnalise));
  Componente.SQL.Add(' and ct.cod_componente = cp.cod_componente order by velocidade');
  Componente.Open;
  Componente.First;
  while not(Componente.Eof) do
    begin
      Write(Arq, Componente.FieldByName('tipo').AsString);
      Write(Arq, BNDO.formata_string(Componente.FieldByName('nome').AsString, 7));
      Write(Arq, FormatFloat('000.0000000', Componente.FieldByName('velocidade').AsFloat));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_1').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_2').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_3').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_4').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_5').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_6').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_7').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_8').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_9').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_10').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_11').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_12').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_13').AsString));
      Write(Arq, FormataIndiceComponente(Componente.FieldByName('indice_14').AsString));
      Write(Arq, Formatfloat('00000.00', Componente.FieldByName('h').AsFloat));
      Write(Arq, Formatfloat('00000.00', Componente.FieldByName('g').AsFloat));
      Writeln(Arq);
      Componente.Next;
    end;
  Componente.Close;
  CloseFile(Arq);
  FreeAndNil(EstMaregr);
  FreeAndNil(AnaliseMares);
  FreeAndNil(Componente);
end;

function TClasseRotinasGenericas.RetornaQtdAlturasUnificadasDll(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs;
  DataIni, DataFim: string): Integer;
var
  AltHor: TADOQuery;
begin
  AltHor:= TADOQuery.Create(nil);
  AltHor.Connection:= Conexao;
  AltHor.SQL.Clear;
  AltHor.SQL.Add('select count(*) as QtdAlturas from vw_alturas_unificadas ' +
    'where cod_estacao_maregrafica = ' + IntToStr(CodEstacao) +
    ' and cod_equipamento_estacao_maregrafica = ' + IntToStr(CodEquipEstacao) +
    ' and dbo.ConverteData(data_hora) >= ''' + BNDO.ConverteDataAnsi(DataIni) +
    ''' and dbo.ConverteData(data_hora) <= ''' + BNDO.ConverteDataAnsi(DataFim) +
    ''' and datepart(mi, data_hora) = 00 and datepart(ss, data_hora) = 00');
  AltHor.Open;
  RetornaQtdAlturasUnificadasDll:= AltHor.FieldByName('QtdAlturas').AsInteger;
  AltHor.Close;
  FreeAndNil(AltHor);
end;

function TClasseRotinasGenericas.RetornaCodigoAnalisePeriodoPadraoUnificada(Conexao: TADOConnection; CodEstacao: PeqInteiroSs): Integer;
var
  Query: TADOQuery;
begin
  Query:= TADOQuery.Create(nil);
  Query.Connection:= Conexao;
  Query.SQL.Clear;
  Query.SQL.Add('select cod_analise_mares from dbo.vw_analise_unificadas where cod_estacao_maregrafica = ' +
    IntToStr(CodEstacao) + ' and periodo_padrao = ''S''');
  Query.Open;
  if (Query.IsEmpty) then
    RetornaCodigoAnalisePeriodoPadraoUnificada:= 0
  else
    RetornaCodigoAnalisePeriodoPadraoUnificada:= Query.FieldByName('cod_analise_mares').AsInteger;
  Query.Close;
  FreeAndNil(Query);
end;

function TClasseRotinasGenericas.RetornaZ0AnaliseUnificada(Conexao: TADOConnection; Area: MinInteiroSs; CodAnalise: Integer): Real;
var
  Query: TADOQuery;
begin
  Query:= TADOQuery.Create(nil);
  Query.Connection:= Conexao;
  Query.SQL.Clear;
  Query.SQL.Add('select z0 from dbo.vw_analise_unificadas where ');
  Query.SQL.Add('area = ' + IntToStr(Area) + ' and cod_analise_mares = ' + IntToStr(CodAnalise));
  Query.Open;
  if (Query.IsEmpty) then
    RetornaZ0AnaliseUnificada:= 0
  else
    RetornaZ0AnaliseUnificada:= Query.FieldByName('z0').AsFloat;
  Query.Close;
  FreeAndNil(Query);
end;

function TClasseRotinasGenericas.ValidaPeriodoAlturasUnificadasDll(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs;
  DataIni, DataFim: string; Area: MinInteiroSs): Boolean;
{Fun��o para validar um per�odo de Alturas Hor�rias. Se o per�odo informado estiver
cadastrado dia a dia sem quebras (dias pertencentes ao per�odo que n�o est�o cadastrados)
no Banco de Dados a fun��o retorna true. As datas devem estar no formato dd/mm/yyyy.}
var
  AltHor: TADOQuery;
  QtdDias, QtdDiasCadastrados: integer;
begin
  AltHor:= TADOQuery.Create(nil);
  AltHor.Connection:= Conexao;
  QtdDias:= RetornaDiferencaPeriodo(StrToDate(DataIni), StrToDate(DataFim), 3) + 1;
  AltHor.SQL.Clear;
  AltHor.SQL.Add('select count(tmp.DiaObservado) as QtdDiasCadastrados from ');
  AltHor.SQL.Add('(select mare.dbo.ConverteData(data_hora) as DiaObservado, count(*) as QtdHorasCheias ');
  case (Area) of
    0: AltHor.SQL.Add('from mare.dbo.vw_alturas_unificadas ');
    1: AltHor.SQL.Add('from mare.dbo.vw_alturas_horarias ');
    2: AltHor.SQL.Add('from mare.dbo.vw_alturas_horarias_temp ');
  else
    AltHor.SQL.Add('from mare.dbo.vw_alturas_unificadas ');
  end;
  AltHor.SQL.Add('where cod_estacao_maregrafica = ' + IntToStr(CodEstacao));
  AltHor.SQL.Add(' and cod_equipamento_estacao_maregrafica = ' + IntToStr(CodEquipEstacao));
  AltHor.SQL.Add(' and mare.dbo.ConverteData(data_hora) >= ''' + BNDO.ConverteDataAnsi(DataIni) + ''' ');
  AltHor.SQL.Add('and mare.dbo.ConverteData(data_hora) <= ''' + BNDO.ConverteDataAnsi(DataFim) + ''' ');
  AltHor.SQL.Add('and datepart(mi ,data_hora) = 00 and datepart(ss ,data_hora) = 00 ');
  AltHor.SQL.Add('group by mare.dbo.ConverteData(data_hora) having count(*) = 24) as tmp');
  AltHor.Open;
  QtdDiasCadastrados:= AltHor.FieldByName('QtdDiasCadastrados').AsInteger;
  AltHor.Close;
  FreeAndNil(AltHor);
  if (QtdDias = QtdDiasCadastrados) then
    ValidaPeriodoAlturasUnificadasDll:= True
  else
    ValidaPeriodoAlturasUnificadasDll:= False;
end;

function TClasseRotinasGenericas.TransfereAlturasTemporarias(Conexao: TADOConnection; CodEquipEstacao: PeqInteiroSs;
  DataIni, DataFim: string): Smallint;
var
  Query: TADOQuery;
begin
  Query:= TADOQuery.Create(nil);
  Query.Connection:= Conexao;
  Query.CommandTimeout:= 0;
  Query.SQL.Clear;
  Query.SQL.Add('exec up_transfere_periodo_alturas_temp ' + IntToStr(CodEquipEstacao) + ', ''' + DataIni + ' 00:00'', ''' +
    DataFim + ' 23:59''');
  Query.Open;
  TransfereAlturasTemporarias:= Query.FieldByName('Status').AsInteger;
  Query.Close;
  FreeAndNil(Query);
end;

function TClasseRotinasGenericas.RetornaQtdAlturasUnificadas(Conexao: TADOConnection; CodEstacao, CodEquipEstacao: PeqInteiroSs;
  DataIni, DataFim: string): Integer;
var
  AltHor: TADOQuery;
begin
  AltHor:= TADOQuery.Create(nil);
  AltHor.Connection:= Conexao;
  AltHor.SQL.Clear;
  AltHor.SQL.Add('select count(*) as QtdAlturas from mare.dbo.vw_alturas_unificadas ');
  AltHor.SQL.Add('where cod_estacao_maregrafica = ' + IntToStr(CodEstacao) + ' ');
  AltHor.SQL.Add('and cod_equipamento_estacao_maregrafica = ' + IntToStr(CodEquipEstacao) + ' ');
  AltHor.SQL.Add('and mare.dbo.ConverteData(data_hora) >= ''' + BNDO.ConverteDataAnsi(DataIni) + ''' ');
  AltHor.SQL.Add('and mare.dbo.ConverteData(data_hora) <= ''' + BNDO.ConverteDataAnsi(DataFim) + '''');
  AltHor.Open;
  RetornaQtdAlturasUnificadas:= AltHor.FieldByName('QtdAlturas').AsInteger;
  AltHor.Close;
  FreeAndNil(AltHor);
end;

function TClasseRotinasGenericas.RetornaCodigoAnaliseReducaoPadrao(Conexao: TADOConnection; CodEstacao: PeqInteiroSs): Integer;
var
  Query: TADOQuery;
begin
  Query:= TADOQuery.Create(nil);
  Query.Connection:= Conexao;
  Query.SQL.Clear;
  Query.SQL.Add('select cod_analise_mares from mare.dbo.reducao_sondagem where cod_estacao_maregrafica = ' +
    IntToStr(CodEstacao) + ' and reducao_padrao = ''S''');
  Query.Open;
  if (Query.IsEmpty) then
    RetornaCodigoAnaliseReducaoPadrao:= 0
  else
    RetornaCodigoAnaliseReducaoPadrao:= Query.FieldByName('cod_analise_mares').AsInteger;
  Query.Close;
  FreeAndNil(Query);
end;

function TClasseRotinasGenericas.TransfereAlturasPrincipais(Conexao: TADOConnection; CodEquipEstacao: PeqInteiroSs;
  DataIni, DataFim: string): Smallint;
var
  Query: TADOQuery;
begin
  Query:= TADOQuery.Create(nil);
  Query.Connection:= Conexao;
  Query.CommandTimeout:= 0;
  Query.SQL.Clear;
  Query.SQL.Add('exec up_transfere_periodo_alturas_principal ' + IntToStr(CodEquipEstacao) + ', ''' + DataIni + ' 00:00'', ''' +
    DataFim + ' 23:59''');
  Query.Open;
  TransfereAlturasPrincipais:= Query.FieldByName('Status').AsInteger;
  Query.Close;
  FreeAndNil(Query);
end;

function TClasseRotinasGenericas.EstacaoComPeriodoPadrao(Conexao: TADOConnection; CodEstacao: PeqInteiroSs): Boolean;
var
  Portos: TADOQuery;
begin
  Portos:= TADOQuery.Create(nil);
  Portos.Connection:= Conexao;
  Portos.SQL.Clear;
  Portos.SQL.Add('select vwam.cod_analise_mares from mare.dbo.vw_analise_mares vwam, (select cod_estacao_maregrafica ');
  Portos.SQL.Add('from mare.dbo.estacao_maregrafica where cod_estacao_maregrafica = ' + IntToStr(CodEstacao) + ') e ');
  Portos.SQL.Add('where vwam.cod_estacao_maregrafica = e.cod_estacao_maregrafica and vwam.periodo_padrao = ''S''');
  Portos.Open;
  if (Portos.IsEmpty) then
    EstacaoComPeriodoPadrao:= False
  else
    EstacaoComPeriodoPadrao:= True;
  Portos.Close;
  FreeAndNil(Portos);
end;

function TClasseRotinasGenericas.RetornaCodigoEstacaoEquipEstacao(Conexao: TADOConnection; CodEquipEstacao: PeqInteiroSs): PeqInteiroSs;
var
  Query: TADOQuery;
begin
  Query:= TADOQuery.Create(nil);
  Query.Connection:= Conexao;
  Query.SQL.Clear;
  Query.SQL.Add('select cod_estacao_maregrafica from mare.dbo.equipamento_estacao_maregrafica ');
  Query.SQL.Add('where cod_equipamento_estacao_maregrafica = ' + IntToStr(CodEquipEstacao));
  Query.Open;
  if (Query.IsEmpty) then
    RetornaCodigoEstacaoEquipEstacao:= 0
  else
    RetornaCodigoEstacaoEquipEstacao:= Query.FieldByName('cod_estacao_maregrafica').AsInteger;
  Query.Close;
  FreeAndNil(Query);
end;

function TClasseRotinasGenericas.RetornaCodigoEquipEstacaoAnalise(Conexao: TADOConnection; CodAnalise: LongoInteiroSs): PeqInteiroSs;
var
  Query: TADOQuery;
begin
  Query:= TADOQuery.Create(nil);
  Query.Connection:= Conexao;
  Query.SQL.Clear;
  Query.SQL.Add('select cod_equipamento_estacao_maregrafica from mare.dbo.analise_mares ');
  Query.SQL.Add('where cod_analise_mares = ' + IntToStr(CodAnalise));
  Query.Open;
  if (Query.IsEmpty) then
    RetornaCodigoEquipEstacaoAnalise:= 0
  else
    RetornaCodigoEquipEstacaoAnalise:= Query.FieldByName('cod_equipamento_estacao_maregrafica').AsInteger;
  Query.Close;
  FreeAndNil(Query);
end;

function TClasseRotinasGenericas.RetornaTaxaAmostragemEquipamento(Conexao: TADOConnection; CodEquipEstacao: PeqInteiroSs): MinInteiroSs;
var
  Query: TADOQuery;
begin
  Query:= TADOQuery.Create(nil);
  Query.Connection:= Conexao;
  Query.SQL.Clear;
  Query.SQL.Add('select taxa_amostragem from mare.dbo.equipamento_estacao_maregrafica ');
  Query.SQL.Add('where cod_equipamento_estacao_maregrafica = ' + IntToStr(CodEquipEstacao));
  Query.Open;
  if (Query.IsEmpty) then
    RetornaTaxaAmostragemEquipamento:= 0
  else
    RetornaTaxaAmostragemEquipamento:= Query.FieldByName('taxa_amostragem').AsInteger;
  Query.Close;
  FreeAndNil(Query);
end;

function TClasseRotinasGenericas.IdentaDataHora(DataHora: TDateTime; QtdMinutos: PeqInteiroSs): TDateTime;
var
  Hora, Min, Seg: PeqInteiroSs;
  HoraAux, MinAux: Extended;
  QtdDias: PeqInteiroSs;
  Data: TDate;
begin
  Data:= StrToDate(FormatDateTime('dd/mm/yyyy', DataHora));
  QtdDias:= 0;
  Hora:= StrToInt(FormatDateTime('hh', DataHora));
  Min:= StrToInt(FormatDateTime('nn', DataHora));
  Seg:= StrToInt(FormatDateTime('ss', DataHora));
  Min:= Min + QtdMinutos;
  if (Min >= 60) then
    begin
      MinAux:= ((Min div 60) * 60);
      Min:= StrToInt(FloatToStr(Min - MinAux));
      Hora:= Hora + StrToInt(FloatToStr(MinAux / 60));
    end;
  if (Hora > 23) then
    begin
      HoraAux:= ((Hora div 24) * 24);
      Hora:= StrToInt(FloatToStr(Hora - HoraAux));
      QtdDias:= StrToInt(FloatToStr(HoraAux / 24));
    end;
  if (QtdDias > 0) then
    Data:= Data + QtdDias;
  IdentaDataHora:= StrToDateTime(DateToStr(Data) + ' ' + IntToStr(Hora) + ':' + IntToStr(Min) + ':' + IntToStr(Seg));
end;

function TClasseRotinasGenericas.RetornaCodigoPorto(Conexao: TADOConnection; CodEstacao: PeqInteiroSs): PeqInteiroSs;
var
  Porto: TADOQuery;
begin
  Porto:= TADOQuery.Create(nil);
  Porto.Connection:= Conexao;
  Porto.SQL.Clear;
  Porto.SQL.Add('select cod_porto from mare.dbo.portos where cod_estacao_maregrafica = ' + IntToStr(CodEstacao));
  Porto.Open;
  if (Porto.IsEmpty) then
    RetornaCodigoPorto:= 0
  else
    RetornaCodigoPorto:= Porto.FieldByName('cod_porto').AsInteger;
  Porto.Close;
  FreeAndNil(Porto);
end;

function TClasseRotinasGenericas.RetornaDiferencaPeriodo(DataInicial, DataFinal: TDateTime; TipoRetorno: MinInteiroSs): Integer;
{Data: 08/09/2009
Objetivo: Fun��o para retornar a diferen�a entre per�odos DataInicial deve ser sempre menor do que DataFinal.
Tipo do retorno:
1 - em minutos;
2 - em horas;
3 - em dias.
}
begin
  if ((DataInicial > DataFinal) or (TipoRetorno = 0) or (TipoRetorno > 3)) then
    RetornaDiferencaPeriodo:= -1
  else
    begin
      case (TipoRetorno) of
        1: RetornaDiferencaPeriodo:= MinutesBetween(DataInicial, DataFinal);
        2: RetornaDiferencaPeriodo:= HoursBetween(DataInicial, DataFinal);
        3: RetornaDiferencaPeriodo:= DaysBetween(DataInicial, DataFinal);
      end;
    end;
end;

procedure TClasseRotinasGenericas.InterpolacaoSpline20(ITempo, DeltaT: MinInteiroSs; ArqDados, ArqSaida: string);
{Data: 08/09/2009
Objetivo: Interpolar um conjunto de dados observados em uma taxa para outra (m�ximo 20 dias minuto a minuto).
Rotina original SPLINE20 (Prof. Geraldo).
Par�metros: ITempo - taxa dos dados de entrada; DeltaT: taxa dos dados de sa�da.}
var
  TArqDados, TArqSaida: TextFile;
  tt, lim: array [1..1000] of Real;
  classe, T, alt: array [1..5000] of Real;
  M: array [1..4] of Real;
  //s: array [1..30000] of Real;
  D, C, g, h, a, B, E, F, K1, K2, m3, m2, tt1: Real;
  s1a, s1b, s1, s2b, s2a, s2, s3a, s3b, s3: Real;
  I, j, k, n, z, cont, ND, truc, v, jj: Integer;
  s: array [1..30000] of Real;
  Leitura: string;
begin
  //REM **********************************************
  //REM Programa de Interporlacao
  //REM Metodo: Polinomio interpolador Spline cubica
  //REM Numero maximo de interpolacoes:20 dias de 1 em 1minuto
  //REM **********************************************
  //CLS
  //SCREEN 12
  //REM *******************************************
  //REM Intervalo de amostragem(em minutos)
  //INPUT "Intervalo de amostragem(em minutos)=", itempo
  //REM *******************************************
  //REM *******************************************
  //REM Intervalo de tempo em minutos a interpolar
  //INPUT "Intervalo de tempo(em minutos) a interpolar =", deltaT
  //REM *******************************************
  //DIM tt(1000), lim(1000), classe(5000)
  tt[1]:= 1;
  for I:= 2 to itempo do
    tt[I]:= tt[I-1] + (1/itempo);
  n:= 1;
  //DIM T(5000), alt(5000)
  //DIM M(4)
  //DIM s%(30000)
  for I:= 1 to 5000 do
    T[I]:= I;
  //INPUT "arquivo de entrada dos dados: ", arq1$
  //OPEN arq1$ FOR INPUT AS #1
  //INPUT "arquivo de saida dos dados: ", arq2$
  //OPEN arq2$ FOR OUTPUT AS #2
  AssignFile(TArqDados, ArqDados);
  AssignFile(TArqSaida, ArqSaida);
  Reset(TArqDados);
  Rewrite(TArqSaida);
  cont:= 1;
  while not (Eof(TArqDados)) do
    begin
      Readln(TArqDados, Leitura);
      alt[cont]:= StrToFloat(Leitura);
      cont:= cont + 1;
    end;
  //DO UNTIL EOF(1)
    //INPUT #1, s
    //alt(cont) = s
    //cont = cont + 1
  //LOOP
  ND:= cont - 1;
  for j:= 1 to (ND - 3) do
    begin
      D:= (alt[j + 1] - alt[j]) / (T[j + 1] - T[j]);
      C:= (alt[j + 2] - alt[j + 1]) / (T[j + 2] - T[j + 1]);
      g:= (alt[j + 3] - alt[j + 2]) / (T[j + 3] - T[j + 2]);
      h:= C;
      a:= (T[j + 2] - T[j]) / 3;
      B:= (T[j + 2] - T[j + 1]) / 6;
      E:= B;
      F:= (T[j + 3] - T[j + 1]) / 3;
      //REM ************************************
      //REM expressao do polinomio
      //REM**************************************
      K1:= C - D;
      K2:= g - h;
      m3:= (E * K1 - a * K2) / (E * B - a * F);
      m2:= (F * K1 - B * K2) / (a * F - B * E);
      M[1]:= 0;
      M[2]:= m2;
      M[3]:= m3;
      M[4]:= 0;
      if (j = (ND - 3)) then
       truc:= 4
      ELSE
        truc:= 2;
      for k:= 2 to truc do
        begin
          v:= 1;
          while (v <= itempo) do
            begin
              tt1:= j + k - 3 + tt[v];
              z:= k + j - 1;
              s1a:= Power((T[z] - tt1), 3) * M[k - 1];
              s1b:= Power((tt1 - T[z - 1]), 3) * M[k];
              s1:= (s1a + s1b) / (6 * (T[z] - T[z - 1]));
              s2a:= (T[z] - tt1) * alt[z - 1];
              s2b:= (tt1 - T[z - 1]) * alt[z];
              s2:= (s2a + s2b) / (T[z] - T[z - 1]);
              s3a:= (T[z] - T[z - 1]) * (T[z] - tt1) * M[k - 1];
              s3b:= (T[z] - T[z - 1]) * (tt1 - T[z - 1]) * M[k];
              s3:= (s3a + s3b) / 6;
              s[n]:= s1 + s2 - s3;
              n:= n + 1;
              v:= v + deltaT;
            end;
        end;
    end;
  s[n]:= alt[j + 2];
  for jj:= 1 to n do
    Writeln(TArqSaida, FloatToStr(Round(s[jj])));
  CloseFile(TArqSaida);
  CloseFile(TArqDados);
end;

function TClasseRotinasGenericas.ExisteTabuaMares(Conexao: TADOConnection; CodPorto, Ano: PeqInteiroSs): Boolean;
var
  TabuasMares: TADOQuery;
begin
  TabuasMares:= TADOQuery.Create(nil);
  TabuasMares.Connection:= Conexao;
  TabuasMares.SQL.Add('select cod_tabua from mare.dbo.tabuas_mares ');
  TabuasMares.SQL.Add('where cod_porto = ' + IntToStr(CodPorto) + ' ');
  TabuasMares.SQL.Add('and ano = ' + IntToStr(Ano));
  TabuasMares.Open;
  if (TabuasMares.IsEmpty) then
    ExisteTabuaMares:= False
  else
    ExisteTabuaMares:= True;
  TabuasMares.Close;
  FreeAndNil(TabuasMares);
end;

function TClasseRotinasGenericas.RetornaParametroString(Linha: string; Separador: Char): string;
{Fun��o que retorna o conte�do de uma string ap�s o separador "sep".}
var
  I, TamLinha, PosSep: Integer;
  LinhaAux: string;
begin
  LinhaAux:= '';
  I:= 1;
  PosSep:= 0;
  TamLinha:= Length(Linha);
  while (I < TamLinha) do
    begin
      if (Linha[I] = Separador) then
        begin
          PosSep:= I + 1;
          Break;
        end;
      I:= I + 1;
    end;
  if (PosSep <> 0) then
    begin
      for I:= PosSep to TamLinha do
        LinhaAux:= LinhaAux + Linha[I];
      RetornaParametroString:= LinhaAux;
    end
  else
    RetornaParametroString:= '';
end;

function TClasseRotinasGenericas.DataPosterior(Dia, DiaPosterior: TDate): Boolean;
{Fun��o que verifica se uma data � realmente o dia posterior � outra.}
begin
  if ((Dia + 1) = DiaPosterior) then
    DataPosterior:= True
  else
    DataPosterior:= False;
end;

function TClasseRotinasGenericas.ValidaDataString(Data: string): Boolean;
{Fun��o que valida uma data. O par�metro 'Data' para ser validado
deve estar primeiramente no formato "dd/mm/aaaa".}
var
  I: Integer;
  CaracterInvalido: Boolean;
begin
  try
    CaracterInvalido:= False;
    for I:= 1 to 10 do
      begin
        if not (Data[I] in ['0'..'9', '/']) then
          begin
            CaracterInvalido:= True;
            Break;
          end;
      end;
    if (CaracterInvalido) then
      ValidaDataString:= False
    else
      begin
        StrToDate(Data);
        ValidaDataString:= True;
      end;
  except
    ValidaDataString:= False;
  end;
end;

procedure TClasseRotinasGenericas.ListaEquipamentos(Conexao: TADOConnection; CodEstacao: PeqInteiroSs; var ComboBox: TCombobox);
var
  EquipEst: TADOQuery;
begin
  ComboBox.Clear;
  EquipEst:= TADOQuery.Create(nil);
  EquipEst.Connection:= Conexao;
  EquipEst.SQL.Add('select ee.*, e.nome_equipamento from mare.dbo.equipamento_estacao_maregrafica ee, ' +
    'equipamento e where ee.cod_estacao_maregrafica = ' + IntToStr(CodEstacao) +
    ' and ee.cod_equipamento = e.cod_equipamento');
  EquipEst.Open;
  while not (EquipEst.Eof) do
    begin
      ComboBox.Items.Add(FormatFloat('000', EquipEst.FieldByName('cod_equipamento').AsInteger) + ' - ' +
        EquipEst.FieldByName('nome_equipamento').AsString);
      EquipEst.Next;
    end;
  EquipEst.Close;
  FreeAndNil(EquipEst);
end;

function TClasseRotinasGenericas.FormataEspacosString(St: string; QtdEspacos: PeqInteiroSs; Direita: Boolean): string;
{Fun��o que preenche com espa�os em branco � direita ou � esquerda de uma string.
Informar no par�metro tam_format o tamanho total da string (com os
espa�os em branco).}
var
  StAux: string;
  I: PeqInteiroSs;
begin
  StAux:= St;
  for I:= (Length(StAux) + 1) to QtdEspacos do
    begin
      if (Direita) then
        StAux:= StAux + ' '
      else
        StAux:= ' ' + StAux;
    end;
  FormataEspacosString:= StAux;
end;

function TClasseRotinasGenericas.ApagaArquivoSeExiste(Arquivo: string): Boolean;
begin
  try
    if (FileExists(Arquivo)) then
      SysUtils.DeleteFile(Arquivo);
    ApagaArquivoSeExiste:= True;
  except
    ApagaArquivoSeExiste:= False;
  end;
end;

procedure TClasseRotinasGenericas.InsereDataHoraDados(DataInicial: TDateTime; ArquivoDados, ArquivoSaida: string;
  TaxaMinutos: MinInteiroSs);
var
  TArqDados, TArqSaida: TextFile;
  DataAux: TDateTime;
  Altura: string;
begin
  AssignFile(TArqDados, ArquivoDados);
  AssignFile(TArqSaida, ArquivoSaida);
  Reset(TArqDados);
  if (FileExists(ArquivoSaida)) then
    Append(TArqSaida)
  else
    Rewrite(TArqSaida);
  DataAux:= DataInicial;
  while not (Eof(TArqDados)) do
    begin
      Readln(TArqDados, Altura);
      Writeln(TArqSaida, FormatDateTime('dd/mm/yyyy', DataAux) + ' ' + FormatDateTime('hh:nn', DataAux) +
        FormataEspacosString(Trim(Altura), 5, False));
      DataAux:= IdentaDataHora(DataAux, TaxaMinutos);
    end;
  CloseFile(TArqDados);
  CloseFile(TArqSaida);
end;

function TClasseRotinasGenericas.SubtraiDataHora(DataHora: TDateTime; QtdMinutos: PeqInteiroSs): TDateTime;
var
  Hora, Min, Seg, QtdDias: Smallint;
  HoraAux, MinAux: Smallint;
  Data: TDate;
begin
  Data:= StrToDate(FormatDateTime('dd/mm/yyyy', DataHora));
  QtdDias:= 0;
  Hora:= StrToInt(FormatDateTime('hh', DataHora));
  Min:= StrToInt(FormatDateTime('nn', DataHora));
  Seg:= StrToInt(FormatDateTime('ss', DataHora));
  Min:= Min - QtdMinutos;
  if (Min < 0) then
    begin
      MinAux:= ((Abs(Min) div 60) * 60);
      if ((Min + MinAux) < 0) then
        MinAux:= MinAux + 60;
      Min:= (Min + MinAux);
      Hora:= Hora - (MinAux div 60);
    end;
  if (Hora < 0) then
    begin
      HoraAux:= ((Abs(Hora) div 24) * 24);
      if ((Hora + HoraAux) < 0) then
        HoraAux:= HoraAux + 24;
      Hora:= (Hora + HoraAux);
      QtdDias:= QtdDias + (HoraAux div 24);
    end;
  if (QtdDias > 0) then
    Data:= Data - QtdDias;
  SubtraiDataHora:= StrToDateTime(DateToStr(Data) + ' ' + IntToStr(Hora) + ':' + IntToStr(Min) + ':' + IntToStr(Seg));
end;

function TClasseRotinasGenericas.ExisteReducaoPadrao(Conexao: TADOConnection; CodEstacao: PeqInteiroSs): Boolean;
var
  Query: TADOQuery;
begin
  Query:= TADOQuery.Create(nil);
  Query.Connection:= Conexao;
  Query.SQL.Add('select cod_estacao_maregrafica from mare.dbo.reducao_sondagem ' +
    'where cod_estacao_maregrafica = ' + IntToStr(CodEstacao) + ' and reducao_padrao = ''S''');
  Query.Open;
  if (Query.IsEmpty) then
    ExisteReducaoPadrao:= False
  else
    ExisteReducaoPadrao:= True;
  Query.Close;
  FreeAndNil(Query);
end;

function TClasseRotinasGenericas.IdentaDataHoraSegundos(DataHora: TDateTime; QtdSegundos: PeqInteiroSs): TDateTime;
var
  Hora, Min, Seg: PeqInteiroSs;
  HoraAux, MinAux, SegAux: Extended;
  QtdDias: PeqInteiroSs;
  Data: TDate;
begin
  Data:= StrToDate(FormatDateTime('dd/mm/yyyy', DataHora));
  QtdDias:= 0;
  Hora:= StrToInt(FormatDateTime('hh', DataHora));
  Min:= StrToInt(FormatDateTime('nn', DataHora));
  Seg:= StrToInt(FormatDateTime('ss', DataHora));
  Seg:= Seg + QtdSegundos;
  if (Seg >= 60) then
    begin
      SegAux:= ((Seg div 60) * 60);
      Seg:= StrToInt(FloatToStr(Seg - SegAux));
      Min:= Min + StrToInt(FloatToStr(SegAux / 60));
    end;
  if (Min >= 60) then
    begin
      MinAux:= ((Min div 60) * 60);
      Min:= StrToInt(FloatToStr(Min - MinAux));
      Hora:= Hora + StrToInt(FloatToStr(MinAux / 60));
    end;
  if (Hora > 23) then
    begin
      HoraAux:= ((Hora div 24) * 24);
      Hora:= StrToInt(FloatToStr(Hora - HoraAux));
      QtdDias:= StrToInt(FloatToStr(HoraAux / 24));
    end;
  if (QtdDias > 0) then
    Data:= Data + QtdDias;
  IdentaDataHoraSegundos:= StrToDateTime(DateToStr(Data) + ' ' + IntToStr(Hora) + ':' + IntToStr(Min) + ':' + IntToStr(Seg));
end;

function TClasseRotinasGenericas.RetornaQtdAlturasEquipamento(Conexao: TADOConnection; CodEquipEstacao: PeqInteiroSs;
  DataInicial, DataFinal: TDate): LongoInteiroSs;
var
  AltHor: TADOQuery;
  TaxaAmostragem: MinInteiroSs;
begin
  TaxaAmostragem:= RetornaTaxaAmostragemEquipamento(Conexao, CodEquipEstacao);
  AltHor:= TADOQuery.Create(nil);
  AltHor.Connection:= Conexao;
  AltHor.SQL.Clear;
  AltHor.SQL.Add('select count(*) as QtdAlturas from mare.dbo.vw_alturas_unificadas ');
  AltHor.SQL.Add('where cod_equipamento_estacao_maregrafica = ' + IntToStr(CodEquipEstacao) + ' ');
  AltHor.SQL.Add('and mare.dbo.ConverteData(data_hora) >= ''' + BNDO.ConverteDataAnsi(FormatDateTime('dd/mm/yyyy', DataInicial)) + ''' ');
  AltHor.SQL.Add('and mare.dbo.ConverteData(data_hora) <= ''' + BNDO.ConverteDataAnsi(FormatDateTime('dd/mm/yyyy', DataFinal)) + ''' ');
  AltHor.SQL.Add('and (datepart(mi ,data_hora) % ' + IntToStr(TaxaAmostragem) + ') = 0 ');
  AltHor.SQL.Add('and datepart(ss ,data_hora) = 0 ');
  AltHor.SQL.Add('and datepart(ms, data_hora) = 0 ');
  AltHor.SQL.Add('and altura is not null');
  AltHor.Open;
  RetornaQtdAlturasEquipamento:= AltHor.FieldByName('QtdAlturas').AsInteger;
  AltHor.Close;
  FreeAndNil(AltHor);
end;

function TClasseRotinasGenericas.RetornaQtdAlturasPeriodo(DataInicial, DataFinal: TDateTime;
  TaxaAmostragem: TAmostragem): Integer;
var
  Cont: Integer;
  DataAux: TDateTime;
begin
  if ((ValidaDataHoraAmostragem(DataInicial, TaxaAmostragem)) and (ValidaDataHoraAmostragem(DataFinal, TaxaAmostragem))) then
    begin
      if (DataInicial <= DataFinal) then
        begin
          Cont:= 1;
          DataAux:= DataInicial;
          while (DataAux <> DataFinal) do
            begin
              DataAux:= IdentaDataHora(DataAux, TaxaAmostragem);
              Cont:= Cont + 1;
            end;
          Result:= Cont;
        end
      else
        Result:= 0;
    end
  else
    Result:= -1;
end;

function TClasseRotinasGenericas.ValidaDataHoraAmostragem(DataHora: TDateTime;
  TaxaAmostragem: TAmostragem): Boolean;
var
  DataAux: TDateTime;
begin
  DataAux:= SysUtils.StrToDateTime(SysUtils.FormatDateTime('dd/mm/yyyy', DataHora));
  while (DataAux < DataHora) do
    DataAux:= IdentaDataHora(DataAux, TaxaAmostragem);
  if (DataAux = DataHora) then
    Result:= True
  else
    Result:= False;
end;

function TClasseRotinasGenericas.ValidaPeriodoAlturasPorEquipamento(Conexao: TADOConnection; Area: TArea;
  CodEquipEstacao: TCodEquipEstacao; DataInicial, DataFinal: TDateTime): Byte;
var
  AltHor: TADOQuery;
  TaxaAmostragem: MinInteiroSs;
  QtdAlturasPeriodo, QtdAlturasQuery: Integer;
begin
  AltHor:= TADOQuery.Create(nil);
  AltHor.Connection:= Conexao;
  TaxaAmostragem:= RetornaTaxaAmostragemEquipamento(Conexao, CodEquipEstacao);
  QtdAlturasPeriodo:= RetornaQtdAlturasPeriodo(DataInicial, DataFinal, TaxaAmostragem);
  AltHor.SQL.Clear;
  AltHor.SQL.Add('select count(*) as QtdAlturas from ');
  case (Area) of
    0: AltHor.SQL.Add('mare.dbo.vw_alturas_unificadas ');
    1: AltHor.SQL.Add('mare.dbo.vw_alturas_horarias ');
    2: AltHor.SQL.Add('mare.dbo.vw_alturas_horarias_temp ');
  end;
  AltHor.SQL.Add('where cod_equipamento_estacao_maregrafica = ' + SysUtils.IntToStr(CodEquipEstacao) + ' ');
	AltHor.SQL.Add('and data_hora >= ''' + SysUtils.FormatDateTime('dd/mm/yyyy hh:nn', DataInicial) + ''' ');
	AltHor.SQL.Add('and data_hora <= ''' + SysUtils.FormatDateTime('dd/mm/yyyy hh:nn', DataFinal) + ''' ');
	AltHor.SQL.Add('and (datepart(mi ,data_hora) % ' + SysUtils.IntToStr(TaxaAmostragem) + ') = 0 ');
	AltHor.SQL.Add('and datepart(ss ,data_hora) = 0 ');
	AltHor.SQL.Add('and datepart(ms, data_hora) = 0 ');
	AltHor.SQL.Add('and altura is not null');
  AltHor.Open;
  QtdAlturasQuery:= AltHor.FieldByName('QtdAlturas').AsInteger;
  AltHor.Close;
  SysUtils.FreeAndNil(AltHor);
  if (QtdAlturasQuery = 0) then
    Result:= 1
  else
    begin
      if (QtdAlturasPeriodo = QtdAlturasQuery) then
        Result:= 0
      else
        Result:= 2;
    end;
end;

function TClasseRotinasGenericas.ValidaDataHoraString(DataHora: string): Boolean;
{Fun��o que valida uma data-hora.}
begin
  try
    SysUtils.StrToDateTime(DataHora);
    Result:= True;
  except
    Result:= False;
  end;
end;

procedure TClasseRotinasGenericas.InterpolacaoSplineFull(ITempo, DeltaT: MinInteiroSs; ArqDados, ArqSaida: string);
{Data: 08/09/2009
Objetivo: Interpolar um conjunto de dados observados em uma taxa para outra (m�ximo 20 dias minuto a minuto).
Rotina original SPLINE20 (Prof. Geraldo).
Par�metros: ITempo - taxa dos dados de entrada; DeltaT: taxa dos dados de sa�da.}
var
  TArqDados, TArqSaida: TextFile;
  tt: array [1..60] of Real;
  T, alt: array of Real;
  M: array [1..4] of Real;
  D, C, g, h, a, B, E, F, K1, K2, m3, m2, tt1: Real;
  s1a, s1b, s1, s2b, s2a, s2, s3a, s3b, s3: Real;
  I, j, k, n, z, cont, ND, truc, v, jj: Integer;
  s: array of Real;
  Leitura: string;
begin
  //REM **********************************************
  //REM Programa de Interporlacao
  //REM Metodo: Polinomio interpolador Spline cubica
  //REM Numero maximo de interpolacoes:20 dias de 1 em 1minuto
  //REM **********************************************
  //CLS
  //SCREEN 12
  //REM *******************************************
  //REM Intervalo de amostragem(em minutos)
  //INPUT "Intervalo de amostragem(em minutos)=", itempo
  //REM *******************************************
  //REM *******************************************
  //REM Intervalo de tempo em minutos a interpolar
  //INPUT "Intervalo de tempo(em minutos) a interpolar =", deltaT
  //REM *******************************************
  //DIM tt(1000), lim(1000), classe(5000)
  SetLength(alt, 2207520);
  SetLength(T, 2207520);
  SetLength(s, 12000000);
  tt[1]:= 1;
  for I:= 2 to itempo do
    tt[I]:= tt[I-1] + (1/itempo);
  n:= 1;
  //DIM T(5000), alt(5000)
  //DIM M(4)
  //DIM s%(30000)
  for I:= 1 to 2207520 do
    T[I]:= I;
  //INPUT "arquivo de entrada dos dados: ", arq1$
  //OPEN arq1$ FOR INPUT AS #1
  //INPUT "arquivo de saida dos dados: ", arq2$
  //OPEN arq2$ FOR OUTPUT AS #2
  AssignFile(TArqDados, ArqDados);
  AssignFile(TArqSaida, ArqSaida);
  Reset(TArqDados);
  Rewrite(TArqSaida);
  cont:= 1;
  while not (Eof(TArqDados)) do
    begin
      Readln(TArqDados, Leitura);
      alt[cont]:= StrToFloat(Leitura);
      cont:= cont + 1;
    end;
  //DO UNTIL EOF(1)
    //INPUT #1, s
    //alt(cont) = s
    //cont = cont + 1
  //LOOP
  ND:= cont - 1;
  for j:= 1 to (ND - 3) do
    begin
      D:= (alt[j + 1] - alt[j]) / (T[j + 1] - T[j]);
      C:= (alt[j + 2] - alt[j + 1]) / (T[j + 2] - T[j + 1]);
      g:= (alt[j + 3] - alt[j + 2]) / (T[j + 3] - T[j + 2]);
      h:= C;
      a:= (T[j + 2] - T[j]) / 3;
      B:= (T[j + 2] - T[j + 1]) / 6;
      E:= B;
      F:= (T[j + 3] - T[j + 1]) / 3;
      //REM ************************************
      //REM expressao do polinomio
      //REM**************************************
      K1:= C - D;
      K2:= g - h;
      m3:= (E * K1 - a * K2) / (E * B - a * F);
      m2:= (F * K1 - B * K2) / (a * F - B * E);
      M[1]:= 0;
      M[2]:= m2;
      M[3]:= m3;
      M[4]:= 0;
      if (j = (ND - 3)) then
       truc:= 4
      ELSE
        truc:= 2;
      for k:= 2 to truc do
        begin
          v:= 1;
          while (v <= itempo) do
            begin
              tt1:= j + k - 3 + tt[v];
              z:= k + j - 1;
              s1a:= Power((T[z] - tt1), 3) * M[k - 1];
              s1b:= Power((tt1 - T[z - 1]), 3) * M[k];
              s1:= (s1a + s1b) / (6 * (T[z] - T[z - 1]));
              s2a:= (T[z] - tt1) * alt[z - 1];
              s2b:= (tt1 - T[z - 1]) * alt[z];
              s2:= (s2a + s2b) / (T[z] - T[z - 1]);
              s3a:= (T[z] - T[z - 1]) * (T[z] - tt1) * M[k - 1];
              s3b:= (T[z] - T[z - 1]) * (tt1 - T[z - 1]) * M[k];
              s3:= (s3a + s3b) / 6;
              s[n]:= s1 + s2 - s3;
              n:= n + 1;
              v:= v + deltaT;
            end;
        end;
    end;
  s[n]:= alt[j + 2];
  for jj:= 1 to n do
    Writeln(TArqSaida, FloatToStr(Round(s[jj])));
  CloseFile(TArqSaida);
  CloseFile(TArqDados);
end;

end.
