Attribute VB_Name = "Module1"
DefDbl A-Z
Global S7$       'Nome do arquivo da estação (text1)
Global WX$       'Disquete e diretório onde estáo arquivo (text2)
Global yw        'Número indicador do tipo da previsão (text3)
Global DI        'Dia inicial da previsão (text4)
Global mes       'Mês inicial da previsão (text5)
Global an        'Ano inicial a previsão (text6)
Global DT        'Intervalo entre os valores previstos (text7)
Global NU        'Número de meses da previsão (text 8)
Global WS7$      'Disquete e diretório onde arquivar a previsão (text9)
Global WZ7$      'Nome do arquivo (text10)
Global NOME$     'Disquete e diretório onde está o programa (text11)
Global CN        'Coeficiente para a conversão de cm/s em nós
Global ES        'Escala do gráfico
Global YE        'Identificador:1 - marés;2 - correntes
Global UNID$     'Identificador do botão:1 - cm/s; 2 - nós
 Function formata(numero As Variant, formato As String) As String

Dim tam As Integer
Dim aux As String

  tam = Len(formato)
  aux = Format$(numero, formato)
  While Len(aux) < tam
    aux = " " + aux
  Wend
  formata = aux

End Function
Sub locate(l, c)

         formGraf.picGraf.CurrentX = c * 8
         formGraf.picGraf.CurrentY = l * 12

End Sub

Sub PREVISÃO()


'                    ***************************************************
'                    *                                                 *
'                    *     Programa PREVISÃO para previsão da maré     *
'                    *            ou da corrente de maré.              *
'                    *                                                 *
'                    *                   A.S. Franco                   *
'                    *                                                 *
'                    ***************************************************
                                                
     Dim A$(450), H(450), BB(450, 4), CC(450, 4), MK(450), b(10), HC(450), GC(450), horas(200)
     Dim X(6000), Y(6000), Q(13), Z(13), G(450), d(13), W2(450), GG$(20), VAR(7), x7(1000)
     Dim F(41), U(41), V(41, 6), W0(41), W(450), W1(12), A7(13, 3), DOOD(450, 7), MINUTOS(200)
     Dim DIAS(4), fase(720), kapa(200), teta(800), falua(800), lonlua(101, 5), ampllua(101)
     Dim lonsol(3, 5), amplsol(3), horalua(5), fasemes(5, 2), dia(5), hora(5), minuto(5)
     Dim faselua(1 To 20) As String, dialua(1 To 20) As Integer, ilua As Integer
     Dim diatroca As Integer, fasetroca As String, numluas As Integer, fl(4) As String

  
     VAR(1) = 14.4920521: VAR(2) = 0.5490165: VAR(3) = 0.0410686
     VAR(4) = 0.0046418: VAR(5) = 0.0022064: VAR(6) = 0.000002: VAR(7) = 0
     d(1) = 0: d(2) = 31: d(3) = 28: d(4) = 31: d(5) = 30: d(6) = 31: d(7) = 30
     d(8) = 31: d(9) = 31: d(10) = 30: d(11) = 31: d(12) = 30: d(13) = 31
     I1 = an Mod 4: I2 = an Mod 400:
     If I1 = 0 And I2 <> 0 Or I3 = 0 Then d(3) = 29
     
     DA7$ = NOME$ + "\" + "CONASTR3.BAS"
     DA7$ = UCase$(DA7$): DB$ = DA7$
     D0 = DI: DTO = DT: DT = DT / 60
     
             
     '******************* LEITURA DE DADOS PARA PREVISÃO **********************
    If yw <> 3 Then Open WS7$ + "\" + WZ7$ For Output As #2

     kr = 1
     
1100 Open WX$ + "\" + S7$ For Input As #1
     Input #1, YY$, YE, Z7$, LA1, LA2, LA$, LO1, LO2, LO$, FU, NC, NM
     If YE = 2 Then Input #1, MM
     If yw = 2 Then DT = 1
     If yw <> 3 And YE = 1 Then DI = 1: HI = -3: N = (d(mes + 1) * 24 + 6) / DT
     If yw = 1 And YE = 2 Then DI = 1: HI = 0: N = (d(mes + 1) * 24) / DT
   If yw = 3 Then
      If YE = 1 Then HI = -3: DT = 1: N = 30
      If YE = 2 Then HI = 0: DT = 1: N = 25
   End If
     If UNID$ = "S" Then CN = 0.0192 Else CN = 1
     LAT$ = Str$(LA1) + Str$(LA2) + " " + LA$: LONGITUDE$ = Str$(LO1) + Str$(LO2) + " " + LO$
  If YY$ = "S" Then
     For i = 1 To NC: Input #1, A$(i): Input #1, MK(i)
     For j = 1 To MK(i): Input #1, BB(i, j): Next j
     For j = 1 To MK(i): Input #1, CC(i, j): Next j
     Input #1, H(i), G(i): If YE = 2 Then Input #1, HC(i), GC(i)
     Next i:
     Close #1:
  End If
  If YY$ <> "S" Then
     For i = 1 To NC: Input #1, A$(i)
     For j = 1 To 7: Input #1, DOOD(i, j): Next j
     Input #1, H(i), G(i)
     Next i: Close #1
  End If
  
     '***************** CÁLCULO DOS ELEMENTOS ASTRONÔMICOS *******************
     NDT = N * DT:
     Call ASTRO4(V(), F(), U(), W(), d(), b(), HI, DI, mes, an, NDT, 0, 0, 1, DA7$, YY$)
     '********CALCULANDO AS ALTURAS OU AS COMPONENTES DA VELOCIDADE DA CORRENTE*******
   If YY$ = "S" Then
    For i = 1 To NC: S = 0: W7 = 0: T = 1
     For j = 1 To MK(i): JJ = BB(i, j): KK = CC(i, j)
     T = T * F(JJ) ^ Abs(KK): S = S + U(JJ) * KK: W7 = W7 + V(JJ, 6) * KK: zz = S
     Next j: W2(i) = W7: W(i) = W7 * DT
     H(i) = T * H(i): S = S - G(i): If S < 0 Then S = S + 360
     G(i) = S
   If YE = 2 Then
     HC(i) = T * HC(i): zz = zz - GC(i): If zz < 0 Then zz = zz + 360
     GC(i) = zz
   End If
'   Debug.Print formata(i, "#####0");
'   Debug.Print formata(H(i), "#####0.###0");
'   Debug.Print formata(HC(i), "#####0.###0");
'   Debug.Print formata(G(i), "#####0.###0");
'   Debug.Print formata(GC(i), "#####0.###0")
   Next i
   End If
If YY$ <> "S" Then
     For i = 1 To NC: S = 0: T = 0
     For j = 1 To 7: S = S + DOOD(i, j) * b(j)
     T = T + DOOD(i, j) * VAR(j): Next j
     W2(i) = T: W(i) = T * DT: S = S - Int(S / 360) * 360: S = S - G(i)
     If S < 0 Then S = S + 360
     G(i) = S
     Next i: Close #1
   End If
    For i = 1 To N: S = 0: zz = 0
     For j = 1 To NC: AA = G(j) * 0.017453
     S = S + H(j) * Cos(AA): G(j) = G(j) + W(j)
     If YE = 2 Then
       AC = GC(j) * 0.017453: zz = zz + HC(j) * Cos(AC)
       GC(j) = GC(j) + W(j)
     End If
     Next j: X(i) = S + NM: Y(i) = zz + MM
    Next i
   '************************** ESCOLHA DO TIPO DE PREVISÃO *************************
    If yw <> 3 Then
     If YE = 1 Then Print #2, "     PREVISÃO DE MARÉ": Print #2,
     If YE = 2 Then Print #2, "   PREVISÃO DE CORRENTE": Print #2,
     Print #2, " Estação:";: Print #2, Z7$
     Print #2, " LAT.:  ";
     Print #2, formata(LA1, "##0");
     Print #2, formata(LA2, "##0.0");: Print #2, " " + LA$
     Print #2, " LONG.: ";
     Print #2, formata(LO1, "##0");
     Print #2, formata(LO2, "##0.0");: Print #2, " " + LO$
     Print #2, " Fuso:"; formata(FU, "##0");: Print #2, " horas"
     If YE = 1 Then Print #2, " Nível médio:"; formata(NM * 0.01, "#0.#0");: Print #2, " m"
     Print #2, " No. de componentes:";: Print #2, formata(NC, "###0"): Print #2,
     Print #2, "Fases da lua para ";: Print #2, formata(mes, "00");: Print #2, "/";
     Print #2, formata(an, "000")
     Print #2, "               Dia  Hora"
     Print #2,
     Call ASTRO4(V(), F(), U(), W(), d(), b(), 0, 1, mes, an, 0, 0, 0, 0, DB$, "N")
     Call fases1(d(), b(), mes, yw)
   End If
 
    If YE = 1 And yw = 1 Then Call P11(X(), d(), N, DTO, HI, DI, mes, an, DT)
       
    If YE = 2 And yw = 1 Then Call P22(X(), Y(), N, HI, DT, DTO, CN, mes, an, UNID$)
    
    If YE = 1 And yw = 2 Then Call P6(X(), d(), N, mes, an)
     
   If yw = 3 Then
     If YE = 1 Then Call P4(X(), Y(), LA1, LA2, LA$, LO1, LO2, LO$, FU, mes, D0, an, Z7$, NM, NC)
     If YE = 2 Then Call P5(X(), Y(), N, D0, mes, an, Z7$, LAT$, LONGITUDE$, FU, NM, MM, CN, NC)

'                            CÁLCULO DAS FASES DA LUA
     
     Call ASTRO4(V(), F(), U(), W(), d(), b(), 0, 1, mes, an, 0, 0, 0, 0, DB$, "N")
     Call fases1(d(), b(), mes, yw)
   End If
   If yw <> 3 And NU > 1 Then
     kr = kr + 1:
     If kr <= NU Then
     Print #2,: Print #2,: Print #2,
     mes = mes + 1: If mes >= 13 Then mes = 1: an = an + 1
     GoTo 1100
     End If
   End If
   
   Close #2

  MsgBox "PREVISÃO TERMINADA !": Close

     End Sub
Function existearq(NOME As String) As Integer

Dim arq As Integer

   On Error GoTo erroarq
   arq = FreeFile
   Open NOME For Input As arq
   If LOF(arq) > 0 Then existearq = True
   Close arq

Exit Function

erroarq:
   Select Case Err
     Case 53, 68, 70, 71, 74, 75, 76
       existearq = False
     Case Else
       existearq = True 'não abriu por alguma outra razão.
   End Select
   Exit Function

End Function
     '******** SUB-ROTINA PARA O CÁLCULO DOS ELEMENTOS ASTRONâMICOS **********
     Sub ASTRO4(V(), F(), U(), W(), d(), b(), HI, DI, mes, an, NDT, T0, JJJ, REF, DA7$, YY$)
'     Debug.Print HI, DI, mes,an
     Close #5
     Open DA7$ For Input As #5
     RJ = 180 / 3.141592: N2 = N / 2: 'NDT=N*DT
'    ANÁLISES CLÁSSICAS: NDT EM HORAS, T0=0, JJJ=1
'    PRIVISåES: T=0, JJJ=0
'    ANÁLISE DO OBSERVAÇÕES POR SATÉLITE: T0, EM DIAS, É DADO DE ENTRADA, JJJ=0
     Dim BW(6), BB1(10), A7(13, 3), Z(13), Q(13), AA(10), AB(10), AC(10), WW(41)
     For i = 1 To 41: For j = 1 To 5: Input #5, V(i, j): Next j: Input #5, WW(i): Next i
     For i = 1 To 13: Input #5, d(i): Next i
     For i = 1 To 13: For j = 1 To 3: Input #5, A7(i, j): Next j: Next i
     I1 = Int(an / 100): MB = an Mod 4: MC = an Mod 100: MD = an Mod 400
     If MB = 0 And MC <> 0 Or MD = 0 Then d(3) = 29
     I2 = I1 - 19:
'                          LONGITUDES PARA O ANO SECULAR
     T1 = 0: T2 = 0: C1 = 0 'Para o século XX
'                 Valores para outros anos seculares (Franco, 1988)
   If I2 <> 0 Then
     T1 = I2: J1 = Abs(I2): C3 = J1 / I2: T2 = T1 * T1 * C3
     C1 = Int(J1 * 0.75 + 0.5) * C3
   End If

     s0 = 277.0224 + 307.8831 * T1 - 0.0011 * T2 - 13.1764 * C1
     h0 = 280.1895 + 0.7689 * T1 + 0.0003 * T2 - 0.9856 * C1
     P0 = 334.3853 + 109.034 * T1 - 0.0103 * T2 - 0.1114 * C1
     NL = 100.7902 + 134.142 * T1 - 0.0021 * T2 - 0.053 * C1
     P1 = 281.2208 + 1.7192 * T1 + 0.00045 * T2 - 0.000047 * C1
     For i = 1 To mes: DI = DI + d(i): Next i
     DI = DI - 1: DI = DI + Int((an - 1601) / 400)
     IA = I1 * 100: BI = an - IA
     AI = Int((BI - 1) * 0.25): If MD = 0 Then AI = AI + 1
     AV = JJJ * NDT / 48: AVI = Int(AV): DA1 = AI + DI
     DA = DA1 + AVI: HI = HI + (AV - AVI) * 24
'                            LONGITUDES PARA A DATA
     b(2) = s0 + 129.38481 * BI + 13.1764 * DA
     b(3) = h0 - 0.23872 * BI + 0.98565 * DA
     b(1) = b(3) - b(2)
     b(4) = P0 + 40.66249 * BI + 0.1114 * DA
     b(5) = 90
     b(6) = NL + 19.32818 * BI + 0.05295 * DA
     b(7) = P1 + 0.01718 * BI + 0.000047 * DA
     b(1) = b(1) + HI * 14.49205211 + (360 - 12.19074939) * T0
     b(2) = b(2) + HI * 0.54902653 + 13.17639673 * T0
     b(3) = b(3) + HI * 0.04106864 + 0.98564734 * T0
     b(4) = b(4) + HI * 0.00464183 + 0.11140408 * T0
     b(6) = b(6) + HI * 0.00220641 + 0.05295392
     b(7) = b(7) + HI * 0.00000196 + 0.00004707 * T0
     DA2 = NDT / 48 + DA1 + HI / 24
     b(8) = P0 + 40.66249 * BI + 0.1114 * DA2
     b(9) = NL + 19.32818 * BI + 0.05295 * DA2
     b(10) = P1 + 0.01718 * BI + 0.000047 * DA2
     If YY$ <> "S" Then b(5) = b(6): b(6) = b(7): b(7) = 90
'     For i = 1 To 6: B(i) = B(i) - Int(B(i) / 360) * 360: Next i
'     Debug.Print B(1), B(2), B(3)
'     Debug.Print B(4), B(5), B(6)
'If NDT = 0 And T0 = 0 And JJJ = 0 And REF = 0 Then Debug.Print HI, BI, DI, DA
If YY$ = "S" Then  'QUANDO É NECESSÁRIO O CÁLCULO DE f E u
     If ES <> 14 Then GoTo 1235
     For i = 1 To 6: BB1(i) = b(i + 1): Next i
     BW(1) = 0.5490165: BW(2) = 0.0410686: BW(3) = 0.004618: BW(4) = 0.0022064: BW(5) = 0.000002
     BW(6) = 0 ':GOTO 222
1235 For i = 2 To 7: BB1(i - 1) = b(i): Next i
'                         CÁLCULO DE f E u E DOS ARGUMENTOS ASTRONÔMICOS
     For i = 1 To 13: S = 0: For j = 1 To 3: S = S + A7(i, j) * b(j + 7): Next j: XX = S * 0.017453
     Z(i) = Cos(XX): Q(i) = Sin(XX): Next i
     For i = 1 To 41: Input #5, MK: For j = 1 To MK: Input #5, AA(j): Next j
     For j = 1 To MK: Input #5, AB(j): Next j: For j = 1 To MK: Input #5, AC(j): Next j
     S = 0: T = 0: For k = 1 To MK: k1 = AC(k): S = S + AA(k) * Z(k1)
     T = T + AB(k) * Q(k1): Next k: S = 1 + S: F(i) = Sqr(S * S + T * T): U(i) = Atn(T / S) * RJ: Next i
     For i = 1 To 41: W7 = 0: For j = 1 To 5
     W7 = W7 + V(i, j) * b(j): Next j: W(i) = WW(i): V(i, 6) = WW(i)
     If i = 14 Or i = 31 Then W7 = W7 + b(10)
     If i = 18 Or i = 33 Then W7 = W7 - b(10)
     U(i) = W7 + U(i): U(i) = U(i) - Int(U(i) / 360) * 360: If U(i) < 0 Then U(i) = U(i) + 360
     Next i:
'     CLS:FOR I=1 TO 41:IF I=18 THEN INPUT UU
'     PRINT USING "#####";I;:PRINT USING "#####.##";F(I),U(I);
'     PRINT USING "######.#######";W(I)
'     NEXT I:END
     '************************************************************************
'     CORREÇÕES PARA COMPONENTES DE FREQUÊNCIAS PRÓXIMAS, PARA ANÁLISES DE SÉRIES CURTAS
     AV = AV * 24: rd = 0.017453
     If JJJ = 1 And NDT <= 4385 Then
        BX1 = 0.0012715 * AV: BX1 = Sin(BX1) / BX1: B2 = 0.0014335 * AV: B2 = Sin(B2) / B2
        B3 = 0.0007168 * AV: B3 = Sin(B3) / B3
          If NDT <= 2450 Then
            C1 = BX1 * 0.19 * F(9) / F(8): A1 = (U(9) - U(8)) * rd
            Call CORR(A1, 0, C1, 0, F(8), U(8))
            C2 = BX1 * 0.19 * F(13) / F(12): A2 = (U(13) - U(12)) * rd
            Call CORR(A2, 0, C2, 0, F(12), U(12))
            C3 = B2 * 0.331 * F(15) / F(17): A3 = (U(15) - U(17)) * rd
               If (NDT < 1368 And REF = 1) Or REF = 0 Then
                  Call CORR(A3, 0, C3, 0, F(17), U(17))
                  C4 = BX1 * 0.19 * F(27) / F(26): A4 = (U(27) - U(26)) * rd
               End If
               If (NDT < 1368 And REF = 1) Or REF = 0 Then
               Call CORR(A4, 0, C4, 0, F(27), U(27))
               End If
          End If
        C5 = B3 * 0.059 * F(31) / F(32): A5 = (U(31) - U(32)) * rd
        C6 = BX1 * 0.271 * F(34) / F(32): A6 = (U(34) - U(32)) * rd
        If NDT >= 1368 And REF = 1 Then A6 = 0: C6 = 0
        Call CORR(A5, A6, C5, C6, F(32), U(32))
     End If
End If
     End Sub
     '********************* SUB-ROTINA CORR **********************************
     Sub CORR(AA, AB, CA, CB, F, U)
     A = 1 + CA * Cos(AA) + CB * Cos(AB): b = CA * Sin(AA) + CB * Sin(AB)
     F = F * Sqr(A * A + b * b): U = U + Atn(b / A) * 57.29578
     U = U - Int(U / 360) * 360: If U < 0 Then U = U + 360
     End Sub
          
     '********** SUB-ROTINA P4 - PREVISAO DA MARÉ PARA UM DIA ****************
     'PRINT :PRINT " EFETUANDO A INTERPOLAÇÃO DA CURVA PREVISTA NUM DIA QUALQUER"
     Sub P4(X(), Y(), LA1, LA2, LA$, LO1, LO2, LO$, FU, mes, D0, an, Z7$, NM, NC)
     Dim P(300), HO(20), MI(20), ALT(20)
     Dim gx As Single, gy As Single
     Dim gx1 As Single, gy1 As Single
'                             CÁLCULO DAS PREAMARES E BAIXAMARES
     IL = 30: M = 0: k = 0: k1 = 0: S = 0: IT = 5: ALT7 = NM
     For i = 4 To IL - 2
     If X(i - 1) <= X(i) And X(i) >= X(i + 1) Then M = i
     If X(i - 1) >= X(i) And X(i) <= X(i + 1) Then M = i
     If i > M Then GoTo 9120
     Call INTERP2(X(), M, IT, P(), KK)
     For j = 2 To KK - 1:
     If P(j - 1) <= P(j) And P(j) >= P(j + 1) Then JJ = j
     If P(j - 1) >= P(j) And P(j) <= P(j + 1) Then JJ = j
     Next j
     hora = M - 5 - 0.5 ^ IT * (2 - JJ): ihora = Int(hora)
     Min = (hora - ihora) * 60 + 0.5: ihora = ihora Mod 24
     If hora < 0 Or hora > 24 Then GoTo 9120
     HO7 = ihora: MI7 = Min
     k1 = k1 + 1: HO(k1) = ihora: MI(k1) = Min: ALT(k1) = 0.01 * P(JJ)
'     Debug.Print HO(K1), MI(K1), ALT(K1)
9120 Next i:
'                           INTERPOLAÇÃO DA CURVA DE MARÉS
     FA = 1 + Int(NM / 100): ES = 1.55 / FA
     JJ = 30: For i = 1 To 5: KK = 2 * JJ - 3
     For j = 1 To JJ - 3: Y(j) = -0.0625 * (X(j) + X(j + 3)) + 0.5625 * (X(j + 1) + X(j + 2)): Next j
     For k = 2 To KK Step 2: KZ = KK - k + 1: X(KZ) = X(JJ - k / 2): X(KZ - 1) = Y(JJ - k / 2 - 2): Next k
     For k = 2 To KK - 1: X(k - 1) = X(k): Next k: JJ = KK: Next i ': Cls

'     Screen 12: Color 9, 15, 4

'                        IMPRESSÃO DAS HORAS E ALTURAS DE PREAMAR E BAIXAMAR
      ii = 1: For i = 1 To k1
         formGraf.picGraf.ForeColor = QBColor(9)
         locate (11.5 + ii), 74.5
       If hox = 0 Or hox <> HO(i) Then
       Debug.Print hox
         formGraf.picGraf.Print formata(HO(i), "##00"); " "; formata(MI(i), "##00"): hox = HO(i)
         locate (11.5 + ii), 84.8: If ALT(i) >= 10 Then locate (11.5 + i), 84
         If ALT(i) < 0 Then locate (11.5 + i), 84.7
         ALT(i) = Int(ALT(i) * 100 + 0.5) / 100
         formGraf.picGraf.Print ALT(i)
         ii = ii + 1
       End If
     Next i
'                TRAÇADO DAS LINHAS RETAS E DA GRADUAÇÃO DA ESCALA DE ALTURAS
     
     formGraf.picGraf.Line (1, 1)-(1, 478):  formGraf.picGraf.Line (4, 3)-(4, 475)
     formGraf.picGraf.Line (512, 1)-(512, 478): formGraf.picGraf.Line (514, 1)-(514, 478)
     formGraf.picGraf.Line (767, 3)-(767, 475): formGraf.picGraf.Line (770, 1)-(770, 478)
     formGraf.picGraf.Line (1, 1)-(772, 1): formGraf.picGraf.Line (3, 3)-(770, 3)
     formGraf.picGraf.Line (1, 478)-(770, 478): formGraf.picGraf.Line (3, 475)-(767, 475)
     
'                                TRAÇADO DA GRADE

     For j = 1 To 470 Step 8: formGraf.picGraf.Line (514, j + 4)-(519, j + 4): Next j
     For j = 1 To 470 Step 40: formGraf.picGraf.Line (514, j + 20)-(524, j + 20): Next j
     formGraf.picGraf.ForeColor = QBColor(2)
     For k = 29 To 468 Step 8: formGraf.picGraf.Line (3, k + 8)-(512, k + 8): Next k
     For k = 24 To 510 Step 20: formGraf.picGraf.Line (k, 37)-(k, 475): Next k
     formGraf.picGraf.ForeColor = QBColor(9)
'                            TRAÇADO DA CURVA DA MARÉ
     For i = 26 To 804
       gy = 424 - X(i) * ES * 1.05: gx = 0.618 * i + 7.5
       gy1 = 424 - X(i + 1) * ES * 1.05: gx1 = 0.618 * (i + 1) + 7.5
       formGraf.picGraf.Line (gx, gy)-(gx1, gy1)
     Next i
'                              IMPRESSÃO DAS HORAS
     formGraf.picGraf.ForeColor = QBColor(0)
     FR = -2: For i = 1 To 13
       FR = FR + 2
       locate 2, 5 * i - 2.7
       formGraf.picGraf.Print formata(FR, "#0");
     Next i
'                   IMPRESSÃO DOS NÚMEROS DA ESCALA DE ALTURAS
     For i = 0 To 5: locate 6.65 * (i - 1) + 8, 65.3
     gama = Int((4 - (i - 1)) * 100 * FA / 2): gama = gama / 100
     formGraf.picGraf.Print gama 'formata((4 - (i - 1)) * FA / 2, "#0,00"):
     Next i
     
'                              IMPRESSÃO DOS RÓTULOS
     locate 0.5, 31.5: formGraf.picGraf.Print "horas"
     locate 14, 70: formGraf.picGraf.Print "A"
     locate 15, 70: formGraf.picGraf.Print "L"
     locate 16, 70: formGraf.picGraf.Print "T"
     locate 17, 70: formGraf.picGraf.Print "U"
     locate 18, 70: formGraf.picGraf.Print "R"
     locate 19, 70: formGraf.picGraf.Print "A"
     locate 20, 70: formGraf.picGraf.Print "S"
     locate 22, 69.6: formGraf.picGraf.Print "(m)"
     locate 2, 74: formGraf.picGraf.Print "LOCAL:" + " " + Z7$
     locate 3, 74: formGraf.picGraf.Print "LATITUDE:";: formGraf.picGraf.Print "  ";
     formGraf.picGraf.Print formata(LA1, "00");: formGraf.picGraf.Print "  ";
     LA2 = Int(10 * LA2) / 10: formGraf.picGraf.Print LA2;
     formGraf.picGraf.Print " " & LA$
     locate 4, 74: formGraf.picGraf.Print "LONGITUDE:";: formGraf.picGraf.Print "  ";
     formGraf.picGraf.Print formata(LO1, "000"); " "; 'formata(LO2, "00,0");
     LO2 = Int(10 * LO2) / 10: formGraf.picGraf.Print LO2;
     formGraf.picGraf.Print " " + LO$
     locate 5, 74: formGraf.picGraf.Print "FUSO";: formGraf.picGraf.Print FU
     locate 6, 74: formGraf.picGraf.Print "DATA:";
     formGraf.picGraf.Print D0;: formGraf.picGraf.Print "/";
     formGraf.picGraf.Print mes;: formGraf.picGraf.Print "/";:
     formGraf.picGraf.Print an
     locate 7, 74: formGraf.picGraf.Print "NÍVEL MÉDIO: ";: NM = Int((NM) + 0.5) / 100
     formGraf.picGraf.Print NM;: formGraf.picGraf.Print "  m"
     locate 8, 74: formGraf.picGraf.Print "No. DE COMPONENTES: ";:
     formGraf.picGraf.Print NC
     locate 10, 72.5: formGraf.picGraf.Print ; "PREAMARES E BAIXAMARES"
     locate 11, 75.5: formGraf.picGraf.Print "horas            alturas(m)"

     formGraf.Show
     
  End Sub
     '******* SUB-ROTINA P5 PARA PREVISÃO DA CORRENTE DE MARÉ NUM DIA QUALQUER ********
     Sub P5(X(), Y(), N, D0, mes, an, Z7$, LAT$, LONGITUDE$, FU, NM, MM, CN, NC)
     Dim Y1(200), XXX(200)
     pi = 4 * Atn(1): rd = 180 / pi
     For i = 1 To N: A = X(i): b = Y(i): X(i) = Sqr(A * A + b * b)
     Y1(i) = A: XXX(i) = b: If Abs(A) = 0 Then A = 1E-32
     ANG = Atn(b / A) * rd: Call QUADR(ANG, A, b)
     Y(i) = ANG
     Next i
     A = X(1)
     For i = 1 To N: For j = i To N: b = X(j + 1): If b > A Then A = b
     Next j
     Next i:
     If CN = 1 Then COEF = 1 Else COEF = 0.02
     If A <= 50 Then FA = 1.05: FAT = 50 * COEF
     If A > 50 And A <= 100 Then FA = 2.1: FAT = 100 * COEF
     If A > 100 And A <= 200 Then FA = 4.2: FAT = 200 * COEF
     If A > 200 And A <= 300 Then FA = 4: FAT = 250 * COEF
     If A > 250 Then FA = 6.3: FAT = 400 * COEF
     ES = 2 / FA

    '                            IMPRESSÃO DO QUADRO
     formGraf.picGraf.ForeColor = QBColor(9)
     formGraf.picGraf.Line (1, 1)-(1, 478): formGraf.picGraf.Line (3, 3)-(3, 475)
     formGraf.picGraf.Line (770, 1)-(770, 475): formGraf.picGraf.Line (767, 3)-(767, 478)
     formGraf.picGraf.Line (1, 1)-(772, 1): formGraf.picGraf.Line (3, 3)-(773, 3)
     formGraf.picGraf.Line (389, 60)-(389, 400): locate 3, 48.43: formGraf.picGraf.Print "N"
     locate 34, 48.43: formGraf.picGraf.Print "S":
     formGraf.picGraf.Line (1, 478)-(770, 478): formGraf.picGraf.Line (3, 475)-(767, 475)
     formGraf.picGraf.Line (30, 237)-(747, 237)
     
'                                    RÓTULOS
     formGraf.picGraf.ForeColor = QBColor(0)
     locate 2, 2: formGraf.picGraf.Print "LOCAL: " + Z7$
     locate 2, 54: formGraf.picGraf.Print "LATITUDE:";: formGraf.picGraf.Print LAT$
     locate 3, 54: formGraf.picGraf.Print "LONGITUDE:";: formGraf.picGraf.Print LONGITUDE$
     locate 4, 54: formGraf.picGraf.Print "FUSO:";
     formGraf.picGraf.Print formata(FU, "####");: formGraf.picGraf.Print "   horas"
     locate 5, 54: formGraf.picGraf.Print "No. DE COMPONENTES: ";
     formGraf.picGraf.Print formata(NC, "####");
     
     locate 3, 2: formGraf.picGraf.Print "DATA:";
     formGraf.picGraf.Print D0;: formGraf.picGraf.Print "/";
     formGraf.picGraf.Print mes;: formGraf.picGraf.Print "/";
     formGraf.picGraf.Print an: locate 27, 22

'                        ESCALA GRÁFICA DAS VELOCIDADES
     locate 4, 2: formGraf.picGraf.Print "ESCALA"
     formGraf.picGraf.Line (70, 55)-(170, 55): locate 5.5, 8.5: formGraf.picGraf.Print "0"
     locate 5.5, 20.4: formGraf.picGraf.Print FAT: locate 4, 21
     If CN = 1 Then formGraf.picGraf.Print "  velocidade em cm/s" Else formGraf.picGraf.Print "  velocidade em nós"
     locate 5, 33
     For i = 1 To 6
     formGraf.picGraf.Line (20 * (i - 1) + 70, 55)-(20 * (i - 1) + 70, 62)
     Next i
     formGraf.picGraf.ForeColor = QBColor(9)

'                            GRÁFICO DAS VELOCIDADES
     For i = 1 To 25
     M = 29.916667 * (i - 2) + 60: formGraf.picGraf.Line (M, 237)-(M, 247) ': Debug.Print i, M
     formGraf.picGraf.ForeColor = QBColor(3)
     formGraf.picGraf.Line (M, 100)-(M, 370)
     formGraf.picGraf.ForeColor = QBColor(9)

     formGraf.picGraf.Line (M, 237)-(M + ES * XXX(i), (237 - ES * Y1(i)))
     Next i
     locate 19.2, 1.5: formGraf.picGraf.Print "W": locate 19.2, 94.4: formGraf.picGraf.Print "E"

     If CN = 1 Then locate 30, 1: formGraf.picGraf.Print "cm/s"
     If CN <> 1 Then locate 30, 1: formGraf.picGraf.Print "nós"
     locate 32, 1: formGraf.picGraf.Print "gr."
     FR = -2: k = -1: For i = 1 To 13: FR = FR + 2: k = k + 2
     formGraf.picGraf.ForeColor = QBColor(0)
     locate 20.5, 7.5 * (i - 1) + 2: formGraf.picGraf.Print formata(FR, "##00")
     formGraf.picGraf.ForeColor = QBColor(0)
     If CN = 1 Then locate 31, 7.5 * (i - 1) + 1.5 Else locate 31, 7.5 * (i - 1) + 2.5
     If CN = 1 Then formGraf.picGraf.Print formata(X(k), "###000")
     If CN <> 1 Then ZETA = Int(CN * X(k) * 10 + 0.5) / 10: formGraf.picGraf.Print ZETA
     locate 32, 7.5 * (i - 1) + 1.5: formGraf.picGraf.Print formata(Y(k), "###000"): Next i
     formGraf.Show
     End Sub
     '************************* SUB-ROTINA INTERP2 ****************************
     Sub INTERP2(X(), MM, N, W(), KL) ': Cls
     Dim Z(300), P(300): For i = 1 To 7: W(i) = X(MM + i - 4): Next i:
     KL = 7: For i = 1 To N
     KK = 2 ^ (i + 1): KL = KL + 2 ^ i
     For j = 1 To KK: Z(j) = -0.0625 * (W(j) + W(j + 3)) + 0.5625 * (W(j + 1) + W(j + 2)): Next j
     k = 0: For l = 1 To KK: k = k + 2: P(k) = Z(l): Next l:
     k = -1: For l = 2 To KK + 2: k = k + 2: P(k) = W(l): Next l
     For l = 1 To KL: W(l) = P(l): Next l
     Next i
     End Sub

     Sub QUADR(ANG, A, b)
        If A <= 0 Then ANG = ANG + 180 Else If A > 0 And b < 0 Then ANG = ANG + 360
     End Sub
    
    Sub int2(X(), nhoras, teta, kapa): ' Debug.Print nhoras
        For i = 2 To N
        dif1 = Abs(X(i - 1) - teta)
        dif2 = Abs(X(i) - teta)
        dif3 = Abs(X(i + 1) - teta)
 '       Debug.Print x(i), teta, dif2
         If dif2 <= dif1 And dif2 <= dif3 Then kapa = i - 1
        Next i
    End Sub
         '************ SUB-ROTINA P11 PARA IMPRESSAO DE ALTURAS COM QUALQUER DT**********
     Sub P11(X(), d(), N, DTO, HI, DI, mes, an, DT)
      Print #2, "   Data    Horas  Alt.(m)"
      Print #2,
     IN7 = -HI / DT + 1: KK = Int(1 / DT + 0.5)
     M = 1: l = -1: For k = IN7 To N - 3 / DT Step KK
     Min = -DTO: R = (k - IN7) Mod 24 / DT: l = l + 1: If l = 24 Then l = l - 24: M = M + 1
     For j = k To k + KK - 1: Min = Min + DTO
     If M > d(mes + 1) Then GoTo 456

     
     Print #2, formata(M, "00"); 'Right$("0" + Trim$(Str$(M)), 2);
     Print #2, "/";
     Print #2, formata(mes, "00"); 'Right$("0" + Trim$(Str$(mes)), 2);
     Print #2, "/";: Print #2, formata(an, "000"); 'Print #2, Right$("000" + Trim$(Str$(an)), 4); "  ";
     Print #2, "  ";
     Print #2, formata(l, "00"); 'Right$("0" + Trim$(Str$(l)), 2);
     Print #2, ":";
     Print #2, formata(Min, "00"); ';Right$("0" + Trim$(Str$(Min)), 2);
     Print #2, formata(X(j) / 100, "##0.#0")
     Next j: Next k
456  End Sub
  '****** SUB-ROTINA P22 PARA IMPRESSAO DE VELOC. E DIR. DA CORRENTE *******
     Sub P22(X(), Y(), N, HI, DT, DTO, CN, mes, an, UNID$)
     pi = 4 * Atn(1): rd = 180 / pi
   For i = 1 To N: A = X(i): b = Y(i): If A = 0 Then A = 1E-32
     X(i) = Sqr(A * A + b * b): ANG = Atn(-b / -A) * rd: Call QUADR(ANG, A, b)
     Y(i) = ANG
   Next i
     KK = Int(1 / DT + 0.5): M = 1: l = -1
        Print #2,:          Print #2, "   Data     Horas  Vel. dir."
        If UNID$ = "N" Then Print #2, "                   cm/s  gr."
        If UNID$ = "S" Then Print #2, "                   nós   gr."
      Print #2,
   For k = 1 To N Step KK
     Min = -DTO: R = (k - 1) Mod 24 / DT: l = l + 1
     If l = 24 Then l = l - 24: M = M + 1

    For j = k To k + KK - 1: Min = Min + DTO
     Print #2, formata(M, "00");
     Print #2, "/";
     Print #2, formata(mes, "00");
     Print #2, "/";: Print #2, formata(an, "0000");
     Print #2, "  ";: Print #2, formata(l, "00");
     Print #2, ":";
     Print #2, formata(Min, "00");
     Print #2, "  ";
     vel = CN * X(j)
     If UNID$ = "N" Then Print #2, formata(X(j), "000");
     If UNID$ = "S" Then Print #2, formata(vel, "0.0");
     Print #2, "  ";
     Print #2, formata(Y(j), "000")
    Next j
   
   Next k
900  End Sub

    '  SUBROTINA P6 PARA CALCUCLO DE HORAS E ALTURAS DE PM E BM
     'PRINT:PRINT "       CALCULANDO PREAMARES E BAIXAMARES DO DIA
     Sub P6(X(), d(), N, mes, an): D7 = d(mes + 1): IT = 5
     Dim P(100), X1(500), X2(500), Y1(500), DIAS(500), LIN(50)
     Dim H1(8, 2), U1(8), H2(8, 2), U2(8), H3(8, 2), U3(8), H4(8, 2), U4(8), H5(8, 2), U5(8)
     Print #2,
     Print #2, "Horas e alturas de PM e BM"
     Print #2,
     For i = d(mes + 1) + 1 To 31: LIN(i) = 0: Next i
230  IL = N: M = 0: ALT = NM: AMINI = NM: AMAXI = NM: ALFA = 0: k = 0: k1 = 0: S = 0: KKK = 0
     For i = 4 To IL - 1
     If X(i - 1) <= X(i) And X(i) >= X(i + 1) Then M = i
     If X(i - 1) >= X(i) And X(i) <= X(i + 1) Then M = i
     If i > M Then GoTo 9185
     Call INTERP2(X(), M, IT, P(), KK)
     For j = 2 To KK - 1: If P(j - 1) <= P(j) And P(j) >= P(j + 1) Then JJ = j
     If P(j - 1) >= P(j) And P(j) <= P(j + 1) Then JJ = j
     Next j
     hora = M - 5 - 0.5 ^ IT * (2 - JJ): ihora = Int(hora)
     Min = (hora - ihora) * 60 + 0.5: IDIA = Int(ihora / 24) + 1: ihora = ihora Mod 24
     If Abs(ALT - P(JJ)) < 0.5 Or ihora < 0 Then GoTo 9185
9143 k1 = k1 + 1: ALT = P(JJ): altm = 0.01 * ALT
     DIAS(k1) = IDIA: If IDIA > d(mes + 1) Then GoTo 9185
     X1(k1) = ihora: X2(k1) = Min: Y1(k1) = altm: DIF = DIAS(k1 - 1) - DIAS(k1)
     Min = Int(Min + 0.5): altm = 0.01 * Int(100 * altm + 0.5)
'     Print #2, formata(DIAS(k1), "#0");
     Print #2, Tab(2); Right$("0" + Trim$(Str$(DIAS(k1))), 2);
     Print #2, "/";
     Print #2, Right$("0" + Trim$(Str$(mes)), 2);
     Print #2, "/";: Print #2, Right$("000" + Trim$(Str$(an)), 4); "  ";
     Print #2, Right$("0" + Trim$(Str$(ihora)), 2);
     Print #2, ":";
     Print #2, Right$("0" + Trim$(Str$(Min)), 2);
     Print #2, formata(altm, "##0.#0")

     If DIF = 0 Then S = S + 1 _
     Else: KKK = KKK + 1: LIN(KKK) = S: S = 1
9155 If ALT < AMAXI Then GoTo 9160
     AMAXI = ALT: IDIMA = IDIA: IHOMA = ihora: IMIMA = Min
9160 If ALT > AMINI Then GoTo 9185
9170 AMINI = ALT: IDIMI = IDIA: IHOMI = ihora: IMIMI = Min
9185 Next i: l = 0
     End Sub

 Sub fases1(d(), b(), mes, yw)
    Dim astrl(100, 5), ampll(100), frlua(100), astrs(3, 5), ampls(3), frsol(3), freq(5)
    Dim angulo1(100), angulo2(3), X(750)
    Dim phase As String
    Open "c:\Pacmare\LONGLUASOL.TXT" For Input As #15
    rd = 4 * Atn(1) / 180
    '                       Leitura DE DADOS ASTRONÔMICOS
    For i = 1 To 100: For j = 1 To 5: Input #15, astrl(i, j) ': Debug.Print astrl(i, j);
        Next j
        Input #15, ampll(i) ': Debug.Print ampll(i)
    Next i
    For i = 1 To 3: For j = 1 To 5: Input #15, astrs(i, j) ': Debug.Print astrs(i, j);
        Next j
    Input #15, ampls(i): Next i ': Debug.Print ampls(i)
    For i = 1 To 5: Input #15, freq(i): Next i
    '                       EXPRESSÃO DA LONGITUDE DA LUA
    For i = 1 To 100: S = 0: T = 0
        For j = 1 To 5: S = S + astrl(i, j) * b(j + 1): Next j
        For j = 1 To 5: T = T + astrl(i, j) * freq(j): Next j
        S = S - Int(S / 360) * 360: If S < 0 Then S = S + 360
        angulo1(i) = S: frlua(i) = T
'        Debug.Print formata(S, "####0");
'        Debug.Print formata(T, "###0.######0")
    Next i
    '                    EXPRESSÃO DA LONGITUDE DO SOL
    For i = 1 To 3: S = 0: T = 0
        For j = 1 To 5: S = S + astrs(i, j) * b(j + 1): Next j
        S = S - Int(S / 360) * 360: If S < 0 Then S = S + 360
        For j = 1 To 5: T = T + astrs(i, j) * freq(j): Next j
        angulo2(i) = S: frsol(i) = T
'        Debug.Print formata(S, "####0");
'        Debug.Print formata(T, "###0.######0")
    Next i
'                    LONGITUDES MÉDIAS E NÚNERO DE HORAS DO MÊS
       s0 = (b(2) - Int(b(2) / 360) * 360) * rd
       h0 = (b(3) - Int(b(3) / 360) * 360) * rd
       ws = 0.5490165 * rd: wh = 0.0410686 * rd: nhoras = d(mes + 1) * 24

'                 CÁLCULO DAS LONGITUDES VERDADEIRAS DA LUA E DO SOL

        locate 33, 75: formGraf.picGraf.Print "Fases da Lua";:
 '       formGraf.picGraf.Print formata(mes, "00");: formGraf.picGraf.Print "/";
 '       formGraf.picGraf.Print formata(an, "000")
        locate 33, 85.6: formGraf.picGraf.Print "Dias    HMG"
    For i = 1 To nhoras: sss = s0: ttt = h0
      For j = 1 To 100: ANG = (angulo1(j)) * rd:
        sss = sss + ampll(j) * Sin(ANG): angulo1(j) = angulo1(j) + frlua(j)
      Next j
      For j = 1 To 3: ANG = (angulo2(j)) * rd
        ttt = ttt + ampls(j) * Sin(ANG): angulo2(j) = angulo2(j) + frsol(j)
      Next j
        X(i) = (sss - ttt) / rd: X(i) = X(i) - Int(X(i) / 360) * 360
        If X(i) >= 0 And X(i) <= 1 Then X(i) = X(i) + 360
'        If x(i) >= 359 And x(i) <= 361 Then Debug.Print i - 1, x(i)
'        If x(i) >= 89 And x(i) <= 91 Then Debug.Print i - 1, x(i)
'        If x(i) >= 179 And x(i) <= 181 Then Debug.Print i - 1, x(i)
'        If x(i) >= 269 And x(i) <= 271 Then Debug.Print i - 1, x(i)
          
          If X(i) >= 360 And X(i) < 361 Then   'LUA NOVA
            d2 = X(i) - 360: delta = X(i) - X(i - 1)
            If yw = 3 Then phase = "Lua nova:          " Else phase = "Lua nova:     "
            dia = 1 + ((i - 1) - d2 / delta) / 24
            hora = (dia - Int(dia)) * 24: minuto = (hora - Int(hora)) * 60
            dia = Int(dia): hora = Int(hora): minuto = Int(minuto + 0.5)
 '           Debug.Print dia, hora, minuto
           If dia <> dia1 Then Call fases(phase, d(), dia, hora, minuto, M, yw, mes): M = M + 1
           dia1 = dia
          End If
           
           If X(i) >= 89 And X(i) < 91 Then   'QUARTO CRESCENTE
            d2 = X(i) - 90: delta = X(i) - X(i - 1)
            phase = "Q. crescente: "
            dia = 1 + ((i - 1) - d2 / delta) / 24
            hora = (dia - Int(dia)) * 24: minuto = (hora - Int(hora)) * 60
            dia = Int(dia): hora = Int(hora): minuto = Int(minuto + 0.5)
'            Debug.Print dia, hora, minuto
            If dia <> dia1 Then Call fases(phase, d(), dia, hora, minuto, M, yw, mes): M = M + 1
            dia1 = dia
           End If
           
           If X(i) >= 179 And X(i) < 181 Then   'LUA CHEIA
            d2 = X(i) - 180: delta = X(i) - X(i - 1)
            If yw = 3 Then phase = "Lua cheia          " Else phase = "Lua cheia:    "
            dia = 1 + ((i - 1) - d2 / delta) / 24
            hora = (dia - Int(dia)) * 24: minuto = (hora - Int(hora)) * 60
            dia = Int(dia): hora = Int(hora): minuto = Int(minuto + 0.5)
  '          Debug.Print dia, hora, minuto
            If dia <> dia1 Then Call fases(phase, d(), dia, hora, minuto, M, yw, mes): M = M + 1
            dia1 = dia
           End If
           
           If X(i) >= 269 And X(i) < 271 Then   'QUARTO MINGUANTE
            d2 = X(i) - 270: delta = X(i) - X(i - 1)
            If yw = 3 Then phase = "Q. minguante:    " Else phase = "Q. minguante: "
            dia = 1 + ((i - 1) - d2 / delta) / 24
            hora = (dia - Int(dia)) * 24: minuto = (hora - Int(hora)) * 60
            dia = Int(dia): hora = Int(hora): minuto = Int(minuto + 0.5)
  '          Debug.Print dia, hora, minuto
            If dia <> dia1 Then Call fases(phase, d(), dia, hora, minuto, M, yw, mes): M = M + 1
            dia1 = dia
           End If
        s0 = s0 + ws: h0 = h0 + wh
    Next i
'       If yw = 3 Then MsgBox "PREVISÃO TERMINADA !"
Close #15
End Sub
    
    Sub fases(phase As String, d(), dia, hora, minuto, M, yw, mes)
   If yw <> 3 Then
    If dia <= d(mes + 1) Then
     Print #2, phase;: Print #2, " ";: Print #2, formata(dia, "00");: Print #2, "  ";
     Print #2, formata(hora, "00");: Print #2, " ";: Print #2, formata(minuto, "00")
    End If
   End If
   If yw = 3 Then
    formGraf.picGraf.ForeColor = QBColor(9)
    If dia <= d(mes + 1) Then
    locate 34.3 + M, 75: formGraf.picGraf.Print phase;
    locate 34.3 + M, 85.5
    formGraf.picGraf.Print ; formata(dia, "00");: formGraf.picGraf.Print "/";
    formGraf.picGraf.Print formata(mes, "00");: formGraf.picGraf.Print "  ";
    formGraf.picGraf.Print formata(hora, "00");:
    formGraf.picGraf.Print "  ";: formGraf.picGraf.Print formata(minuto, "00")
'    Debug.Print dia, hora, minuto
    End If
   End If

   End Sub
